package com.banking.account.service;

import com.banking.account.domain.entity.Account;
import com.banking.account.domain.entity.Transaction;
import com.banking.account.domain.enums.TransactionType;
import com.banking.account.repository.AccountRepository;
import com.banking.account.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final Random random;

    private static final DateTimeFormatter REF_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private record TransactionTemplate(String description, TransactionType type, BigDecimal minAmount, BigDecimal maxAmount, int weight) {}
    private static final List<TransactionTemplate> CREDIT_TEMPLATES = List.of(
        new TransactionTemplate("Salary Credit", TransactionType.CREDIT, new BigDecimal("3000.00"), new BigDecimal("5500.00"), 25),
        new TransactionTemplate("Transfer Received", TransactionType.CREDIT, new BigDecimal("100.00"), new BigDecimal("2000.00"), 20),
        new TransactionTemplate("Interest Payment", TransactionType.CREDIT, new BigDecimal("10.00"), new BigDecimal("150.00"), 10),
        new TransactionTemplate("Refund", TransactionType.CREDIT, new BigDecimal("25.00"), new BigDecimal("300.00"), 10),
        new TransactionTemplate("Bonus", TransactionType.CREDIT, new BigDecimal("200.00"), new BigDecimal("1000.00"), 5)
    );

    private static final List<TransactionTemplate> DEBIT_TEMPLATES = List.of(
        new TransactionTemplate("Bill Payment - Electricity", TransactionType.DEBIT, new BigDecimal("50.00"), new BigDecimal("200.00"), 20),
        new TransactionTemplate("Bill Payment - Water", TransactionType.DEBIT, new BigDecimal("30.00"), new BigDecimal("100.00"), 15),
        new TransactionTemplate("Bill Payment - Internet", TransactionType.DEBIT, new BigDecimal("60.00"), new BigDecimal("120.00"), 15),
        new TransactionTemplate("ATM Withdrawal", TransactionType.DEBIT, new BigDecimal("20.00"), new BigDecimal("500.00"), 20),
        new TransactionTemplate("Transfer Sent", TransactionType.DEBIT, new BigDecimal("50.00"), new BigDecimal("1500.00"), 15),
        new TransactionTemplate("Service Fee", TransactionType.DEBIT, new BigDecimal("5.00"), new BigDecimal("25.00"), 10),
        new TransactionTemplate("Shopping", TransactionType.DEBIT, new BigDecimal("20.00"), new BigDecimal("300.00"), 15),
        new TransactionTemplate("Restaurant", TransactionType.DEBIT, new BigDecimal("15.00"), new BigDecimal("100.00"), 10)
    );

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.random = new Random();
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactions(String accountNumber, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return transactionRepository.findByAccount_AccountNumberAndTransactionDateBetween(accountNumber, from, to, pageable);
    }

    @Transactional
    public Page<Transaction> getOrGenerateTransactions(String accountNumber, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        if (!transactionRepository.existsByAccount_AccountNumber(accountNumber)) {
            generateSampleTransactions(accountNumber);
        }
        return transactionRepository.findByAccount_AccountNumberAndTransactionDateBetween(accountNumber, from, to, pageable);
    }

    private void generateSampleTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElse(null);
        if (account == null) {
            return;
        }

        int seed = accountNumber.hashCode();
        Random seededRandom = new Random(seed);
        int transactionCount = 15 + seededRandom.nextInt(6);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(90);

        List<Transaction> transactions = new ArrayList<>();
        long totalMinutes = java.time.Duration.between(startDate, endDate).toMinutes();

        for (int i = 0; i < transactionCount; i++) {
            long randomMinutes = seededRandom.nextLong() % totalMinutes;
            LocalDateTime txDate = startDate.plusMinutes(Math.abs(randomMinutes));

            boolean isCredit = seededRandom.nextDouble() < 0.35;
            TransactionTemplate template = selectTemplate(isCredit ? CREDIT_TEMPLATES : DEBIT_TEMPLATES, seededRandom);
            BigDecimal amount = generateAmount(template.minAmount(), template.maxAmount(), seededRandom);

            String reference = "TXN" + txDate.format(REF_FORMATTER) + String.format("%04d", i);

            Transaction transaction = new Transaction(
                account,
                amount,
                template.type(),
                template.description(),
                reference,
                txDate
            );
            transactions.add(transaction);
        }

        transactionRepository.saveAll(transactions);
    }

    private TransactionTemplate selectTemplate(List<TransactionTemplate> templates, Random rnd) {
        int totalWeight = templates.stream().mapToInt(TransactionTemplate::weight).sum();
        int value = rnd.nextInt(totalWeight);
        int cumulative = 0;
        for (TransactionTemplate t : templates) {
            cumulative += t.weight();
            if (value < cumulative) {
                return t;
            }
        }
        return templates.get(0);
    }

    private BigDecimal generateAmount(BigDecimal min, BigDecimal max, Random rnd) {
        double range = max.subtract(min).doubleValue();
        double amount = min.doubleValue() + (rnd.nextDouble() * range);
        return BigDecimal.valueOf(amount).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
