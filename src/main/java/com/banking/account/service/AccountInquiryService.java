package com.banking.account.service;

import com.banking.account.domain.entity.Account;
import com.banking.account.domain.entity.Transaction;
import com.banking.account.domain.embeddable.AccountBalance;
import com.banking.account.dto.AccountDetails;
import com.banking.account.dto.AccountStatement;
import com.banking.account.dto.AccountStatementFilter;
import com.banking.account.dto.AccountHolderSummary;
import com.banking.account.domain.entity.AccountHolder;
import com.banking.account.repository.AccountHolderRepository;
import com.banking.account.repository.AccountRepository;
import com.banking.account.exception.AccountNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountInquiryService {

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;

    public AccountInquiryService(AccountRepository accountRepository,
                                 AccountHolderRepository accountHolderRepository,
                                 CustomerRepository customerRepository,
                                 TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.customerRepository = customerRepository;
        this.transactionService = transactionService;
    }

    @Transactional(readOnly = true)
    public AccountDetails getAccountDetails(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        AccountDetails details = new AccountDetails();
        details.setId(account.getId());
        details.setAccountNumber(account.getAccountNumber());
        details.setType(account.getType());
        details.setStatus(account.getStatus());
        details.setCurrency(account.getCurrency());
        details.setAccountBalance(new AccountBalance(account.getBalance(), account.getBalance(), BigDecimal.ZERO, account.getCurrency())); // String currency already
        details.setProductId(account.getProductId());
        details.setOpenedAt(account.getOpenedAt());
        details.setClosedAt(account.getClosedAt());

        List<AccountHolder> holders = accountHolderRepository.findByAccount_Id(account.getId());
        List<AccountHolderSummary> holderSummaries = holders.stream()
            .map(holder -> {
                AccountHolderSummary summary = new AccountHolderSummary();
                summary.setId(holder.getId());
                summary.setCustomerName(holder.getCustomer().getName());
                summary.setRole(holder.getRole());
                summary.setStatus(holder.getStatus() != null ? holder.getStatus() : com.banking.account.domain.enums.AccountHolderStatus.ACTIVE);
                summary.setEffectiveFrom(holder.getEffectiveFrom());
                return summary;
            })
            .collect(Collectors.toList());
        details.setHolders(holderSummaries);

        return details;
    }

    @Transactional(readOnly = true)
    public AccountBalance getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return new AccountBalance(account.getBalance(), account.getBalance(), BigDecimal.ZERO, account.getCurrency());
    }

    @Transactional
    public AccountStatement getAccountStatement(String accountNumber, AccountStatementFilter filter, Pageable pageable) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        LocalDate fromDate = filter.getFromDate() != null ? filter.getFromDate() : LocalDate.now().minusDays(30);
        LocalDate toDate = filter.getToDate() != null ? filter.getToDate() : LocalDate.now();

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

        Page<Transaction> transactionPage = transactionService.getOrGenerateTransactions(
            accountNumber, fromDateTime, toDateTime, pageable);

        List<Transaction> transactions = transactionPage.getContent();

        BigDecimal totalCredits = transactions.stream()
            .filter(t -> t.getType() == com.banking.account.domain.enums.TransactionType.CREDIT)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebits = transactions.stream()
            .filter(t -> t.getType() == com.banking.account.domain.enums.TransactionType.DEBIT)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal closingBalance = account.getBalance();
        BigDecimal openingBalance = closingBalance.subtract(totalCredits).add(totalDebits);

        AccountStatement statement = new AccountStatement();
        statement.setAccountNumber(accountNumber);
        statement.setFromDate(fromDate);
        statement.setToDate(toDate);
        statement.setOpeningBalance(openingBalance);
        statement.setClosingBalance(closingBalance);
        statement.setTotalCredits(totalCredits);
        statement.setTotalDebits(totalDebits);

        List<AccountStatement.TransactionEntry> entries = transactions.stream()
            .map(t -> {
                AccountStatement.TransactionEntry entry = new AccountStatement.TransactionEntry();
                entry.setId(t.getReference());
                entry.setDate(t.getTransactionDate().toLocalDate());
                entry.setDescription(t.getDescription());
                entry.setAmount(t.getAmount());
                entry.setType(t.getType().name());
                return entry;
            })
            .collect(Collectors.toList());
        statement.setTransactions(entries);

        return statement;
    }
}
