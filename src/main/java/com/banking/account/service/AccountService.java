package com.banking.account.service;

import com.banking.account.domain.entity.Account;
import com.banking.account.domain.entity.AccountHolder;
import com.banking.account.domain.entity.CurrentAccount;
import com.banking.account.domain.entity.SavingsAccount;
import com.banking.account.domain.entity.FixedDepositAccount;
import com.banking.account.domain.entity.LoanAccount;
import com.banking.account.exception.AccountNotFoundException;
import com.banking.account.exception.DuplicateAccountException;
import com.banking.account.exception.InvalidAccountStateException;
import com.banking.account.mapper.AccountMapper;
import com.banking.account.repository.AccountHolderRepository;
import com.banking.account.repository.AccountRepository;
import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountHolderRole;
import com.banking.account.domain.enums.AccountHolderStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.dto.AccountOpeningRequest;
import com.banking.account.dto.AccountHolderRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.customer.api.CustomerQueryService;
import com.banking.customer.api.dto.CustomerDTO;
import com.banking.product.api.ProductQueryService;
import com.banking.product.api.dto.ProductVersionDTO;
import com.banking.limits.api.LimitCheckService;
import com.banking.limits.api.dto.LimitCheckResult;
import com.banking.limits.service.LimitAssignmentService;
import com.banking.limits.dto.response.ProductLimitResponse;
import com.banking.masterdata.repository.CurrencyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final CustomerQueryService customerQueryService;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountMapper accountMapper;
    private final ProductQueryService productQueryService;
    private final LimitCheckService limitCheckService;
    private final LimitAssignmentService limitAssignmentService;
    private final CurrencyRepository currencyRepository;

    public AccountService(AccountRepository accountRepository,
                          AccountHolderRepository accountHolderRepository,
                          CustomerQueryService customerQueryService,
                          AccountNumberGenerator accountNumberGenerator,
                          AccountMapper accountMapper,
                          ProductQueryService productQueryService,
                          LimitCheckService limitCheckService,
                          LimitAssignmentService limitAssignmentService,
                          CurrencyRepository currencyRepository) {
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.customerQueryService = customerQueryService;
        this.accountNumberGenerator = accountNumberGenerator;
        this.accountMapper = accountMapper;
        this.productQueryService = productQueryService;
        this.limitCheckService = limitCheckService;
        this.limitAssignmentService = limitAssignmentService;
        this.currencyRepository = currencyRepository;
    }

    public Page<AccountResponse> getAccounts(String search, AccountType type, AccountStatus status, Long customerId, Pageable pageable) {
        return accountRepository.searchAccounts(search, type, status, customerId, pageable)
            .map(accountMapper::toResponse);
    }

    @Transactional
    public AccountResponse openAccount(AccountOpeningRequest request) {
        CustomerDTO customerDTO = customerQueryService.findById(request.getCustomerId());
        if (customerDTO == null) {
            throw new AccountNotFoundException("Customer not found: " + request.getCustomerId());
        }
        Long customerId = customerDTO.getId();

        com.banking.masterdata.domain.entity.Currency masterDataCurrency = 
            currencyRepository.findById(request.getCurrency())
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency code: " + request.getCurrency()));
        
        if (!masterDataCurrency.isActive()) {
            throw new IllegalArgumentException("Currency is inactive: " + request.getCurrency());
        }

        Optional<ProductVersionDTO> productVersion = productQueryService.findActiveVersionByCode(request.getProductCode());

        Long productId = null;
        Long productVersionId = null;
        String productName = null;

        if (productVersion.isPresent()) {
            productId = productVersion.get().getProductId();
            productVersionId = productVersion.get().getId();
            productName = productVersion.get().getProductName();
        }

        String accountNumber;
        do {
            accountNumber = accountNumberGenerator.generate();
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());

        Account account;
        switch (request.getType()) {
            case CURRENT:
                account = new CurrentAccount(
                    accountNumber,
                    customerId,
                    productId,
                    request.getCurrency(),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
                );
                break;
            case SAVINGS:
                account = new SavingsAccount(
                    accountNumber,
                    customerId,
                    productId,
                    request.getCurrency(),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
                );
                break;
            case FIXED_DEPOSIT:
                account = new FixedDepositAccount(
                    accountNumber,
                    customerId,
                    productId,
                    request.getCurrency(),
                    12,
                    request.getInitialDeposit()
                );
                break;
            case LOAN:
                account = new LoanAccount(
                    accountNumber,
                    customerId,
                    productId,
                    request.getCurrency(),
                    request.getInitialDeposit(),
                    BigDecimal.ZERO,
                    12
                );
                break;
            default:
                throw new IllegalArgumentException("Unsupported account type: " + request.getType());
        }

        account.setProductVersionId(productVersionId);
        account.setProductName(productName);
        account.setBalance(request.getInitialDeposit());
        account.setStatus(AccountStatus.ACTIVE);

        LimitCheckResult limitResult = limitCheckService.checkLimit(
                customerId,
                "PER_TRANSACTION",
                request.getInitialDeposit() != null ? request.getInitialDeposit() : BigDecimal.ZERO,
                request.getCurrency() != null ? request.getCurrency() : "USD"
        );

        if (!limitResult.isApproved()) {
            throw new InvalidAccountStateException("LIMIT_001", "Account opening exceeds limits: " + limitResult.getReason());
        }

        account = accountRepository.save(account);

        List<ProductLimitResponse> productLimits = limitAssignmentService.getProductLimits(request.getProductCode());
        for (ProductLimitResponse productLimit : productLimits) {
            try {
                limitAssignmentService.assignToAccount(
                    productLimit.getLimitDefinitionId(),
                    accountNumber,
                    productLimit.getOverrideAmount()
                );
            } catch (Exception e) {
                log.warn("Failed to assign limit {} to account {}: {}", productLimit.getLimitDefinitionId(), accountNumber, e.getMessage());
            }
        }

        for (AccountHolderRequest holderReq : request.getHolders()) {
            CustomerDTO holderCustomerDTO = customerQueryService.findById(holderReq.getCustomerId());
            if (holderCustomerDTO == null) {
                throw new AccountNotFoundException("Holder customer not found: " + holderReq.getCustomerId());
            }

            AccountHolder holder = new AccountHolder(account, holderCustomerDTO.getId(), holderReq.getRole(), LocalDate.now(), AccountHolderStatus.ACTIVE);
            accountHolderRepository.save(holder);
        }

        return accountMapper.toResponse(account);
    }

    @Transactional
    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw InvalidAccountStateException.alreadyClosed();
        }

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw InvalidAccountStateException.cannotCloseFrozen();
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw InvalidAccountStateException.nonZeroBalance();
        }
        account.setStatus(AccountStatus.CLOSED);
        account.setClosedAt(java.time.LocalDateTime.now());
        accountRepository.save(account);
    }

    @Transactional
    public void freezeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw InvalidAccountStateException.cannotFreezeClosed();
        }

        account.setStatus(AccountStatus.FROZEN);
        accountRepository.save(account);
    }

    @Transactional
    public void unfreezeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (account.getStatus() != AccountStatus.FROZEN) {
            throw InvalidAccountStateException.notFrozen();
        }

        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    @Transactional
    public void addAccountHolder(String accountNumber, AccountHolderRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

            CustomerDTO customerDTO = customerQueryService.findById(request.getCustomerId());
            if (customerDTO == null) {
                throw new AccountNotFoundException("Customer not found: " + request.getCustomerId());
            }
            Long customerId = customerDTO.getId();

        // Check if already a holder
        boolean exists = accountHolderRepository.findByAccount_Id(account.getId()).stream()
            .anyMatch(h -> h.getCustomerId().equals(request.getCustomerId()) && h.getStatus() == com.banking.account.domain.enums.AccountHolderStatus.ACTIVE);
        if (exists) {
            throw InvalidAccountStateException.alreadyHolderActive();
        }

            AccountHolder holder = new AccountHolder(account, customerId, request.getRole(), LocalDate.now(), AccountHolderStatus.ACTIVE);
        accountHolderRepository.save(holder);
    }

    @Transactional
    public void removeAccountHolder(Long holderId) {
        AccountHolder holder = accountHolderRepository.findById(holderId)
            .orElseThrow(() -> new AccountNotFoundException("AccountHolder not found: " + holderId));

        if (holder.getStatus() == com.banking.account.domain.enums.AccountHolderStatus.REMOVED) {
            throw InvalidAccountStateException.holderAlreadyRemoved();
        }

        holder.setStatus(com.banking.account.domain.enums.AccountHolderStatus.REMOVED);
        holder.setEffectiveTo(LocalDate.now());
        accountHolderRepository.save(holder);
    }

    @Transactional
    public void updateAccountHolderRole(Long holderId, AccountHolderRole newRole) {
        AccountHolder holder = accountHolderRepository.findById(holderId)
            .orElseThrow(() -> new AccountNotFoundException("AccountHolder not found: " + holderId));

        if (holder.getStatus() != com.banking.account.domain.enums.AccountHolderStatus.ACTIVE) {
            throw InvalidAccountStateException.inactiveHolderRole();
        }

        holder.setRole(newRole);
        accountHolderRepository.save(holder);
    }
}
