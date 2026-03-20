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
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final CustomerRepository customerRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository,
                          AccountHolderRepository accountHolderRepository,
                          CustomerRepository customerRepository,
                          AccountNumberGenerator accountNumberGenerator,
                          AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.customerRepository = customerRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.accountMapper = accountMapper;
    }

    public Page<AccountResponse> getAccounts(String search, AccountType type, AccountStatus status, Long customerId, Pageable pageable) {
        return accountRepository.searchAccounts(search, type, status, customerId, pageable)
            .map(accountMapper::toResponse);
    }

    @Transactional
    public AccountResponse openAccount(AccountOpeningRequest request) {
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new AccountNotFoundException("Customer not found: " + request.getCustomerId()));

        // Generate unique account number
        String accountNumber;
        do {
            accountNumber = accountNumberGenerator.generate();
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        // Note: The loop is safe because generator produces unique daily numbers, but we double-check

        // Create account based on type
        Account account;
        switch (request.getType()) {
            case CURRENT:
                account = new CurrentAccount(
                    accountNumber,
                    customer,
                    request.getProductId(),
                    request.getCurrency(),
                    BigDecimal.ZERO, // balance starts at 0
                    BigDecimal.ZERO  // overdraftLimit, will set deposit later
                );
                break;
            case SAVINGS:
                account = new SavingsAccount(
                    accountNumber,
                    customer,
                    request.getProductId(),
                    request.getCurrency(),
                    BigDecimal.ZERO, // minimumBalance, placeholder
                    BigDecimal.ZERO  // interestRate, placeholder
                );
                break;
            case FIXED_DEPOSIT:
                account = new FixedDepositAccount(
                    accountNumber,
                    customer,
                    request.getProductId(),
                    request.getCurrency(),
                    12, // depositTerm, placeholder (1 year)
                    request.getInitialDeposit() // maturityAmount, approximate
                );
                break;
            case LOAN:
                account = new LoanAccount(
                    accountNumber,
                    customer,
                    request.getProductId(),
                    request.getCurrency(),
                    request.getInitialDeposit(), // loanAmount
                    BigDecimal.ZERO, // interestRate, placeholder
                    12 // term, placeholder (1 year)
                );
                break;
            default:
                throw new IllegalArgumentException("Unsupported account type: " + request.getType());
        }

        // Set initial balance from initial deposit
        account.setBalance(request.getInitialDeposit());
        account.setStatus(AccountStatus.ACTIVE);
        account = accountRepository.save(account);

        // Create account holders
        for (AccountHolderRequest holderReq : request.getHolders()) {
            Customer holderCustomer = customerRepository.findById(holderReq.getCustomerId())
                .orElseThrow(() -> new AccountNotFoundException("Holder customer not found: " + holderReq.getCustomerId()));

            AccountHolder holder = new AccountHolder(account, holderCustomer, holderReq.getRole(), LocalDate.now(), AccountHolderStatus.ACTIVE);
            accountHolderRepository.save(holder);
        }

        return accountMapper.toResponse(account);
    }

    @Transactional
    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new InvalidAccountStateException("Account is already closed");
        }

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new InvalidAccountStateException("Cannot close a frozen account");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidAccountStateException("Cannot close account with non-zero balance");
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
            throw new InvalidAccountStateException("Cannot freeze a closed account");
        }

        account.setStatus(AccountStatus.FROZEN);
        accountRepository.save(account);
    }

    @Transactional
    public void unfreezeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (account.getStatus() != AccountStatus.FROZEN) {
            throw new InvalidAccountStateException("Account is not frozen");
        }

        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    @Transactional
    public void addAccountHolder(String accountNumber, AccountHolderRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new AccountNotFoundException("Customer not found: " + request.getCustomerId()));

        // Check if already a holder
        boolean exists = accountHolderRepository.findByAccount_Id(account.getId()).stream()
            .anyMatch(h -> h.getCustomer().getId().equals(request.getCustomerId()) && h.getStatus() == com.banking.account.domain.enums.AccountHolderStatus.ACTIVE);
        if (exists) {
            throw new InvalidAccountStateException("Customer is already an active account holder");
        }

        AccountHolder holder = new AccountHolder(account, customer, request.getRole(), LocalDate.now(), AccountHolderStatus.ACTIVE);
        accountHolderRepository.save(holder);
    }

    @Transactional
    public void removeAccountHolder(Long holderId) {
        AccountHolder holder = accountHolderRepository.findById(holderId)
            .orElseThrow(() -> new AccountNotFoundException("AccountHolder not found: " + holderId));

        if (holder.getStatus() == com.banking.account.domain.enums.AccountHolderStatus.REMOVED) {
            throw new InvalidAccountStateException("Account holder already removed");
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
            throw new InvalidAccountStateException("Cannot update role of inactive holder");
        }

        holder.setRole(newRole);
        accountHolderRepository.save(holder);
    }
}
