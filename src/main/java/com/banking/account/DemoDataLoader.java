package com.banking.account;

import com.banking.account.domain.entity.AccountHolder;
import com.banking.account.domain.entity.CurrentAccount;
import com.banking.account.domain.entity.FixedDepositAccount;
import com.banking.account.domain.entity.LoanAccount;
import com.banking.account.domain.entity.SavingsAccount;
import com.banking.account.domain.enums.AccountHolderRole;
import com.banking.account.domain.enums.AccountHolderStatus;
import com.banking.account.domain.enums.Currency;
import com.banking.account.repository.AccountHolderRepository;
import com.banking.account.repository.AccountRepository;
import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.SMECustomer;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Order(2)
@Profile("!test")
public class DemoDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataLoader.class);

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final CustomerRepository customerRepository;

    public DemoDataLoader(AccountRepository accountRepository,
                          AccountHolderRepository accountHolderRepository,
                          CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        if (accountRepository.count() > 0) {
            log.info("Account demo data already exists, skipping.");
            return;
        }

        log.info("Loading account demo data...");

        // Create demo customers if they don't exist
        if (customerRepository.count() == 0) {
            log.info("Creating demo customers...");
            Customer corporate = new CorporateCustomer("CUST-001", "Acme Corporation", CustomerStatus.ACTIVE);
            corporate = customerRepository.save(corporate);
            log.info("Created CorporateCustomer CUST-001");

            Customer sme = new SMECustomer("CUST-002", "Joe's Bakery Ltd", CustomerStatus.ACTIVE);
            sme = customerRepository.save(sme);
            log.info("Created SMECustomer CUST-002");

            Customer individual = new IndividualCustomer("CUST-003", "Jane Smith", CustomerStatus.ACTIVE);
            individual = customerRepository.save(individual);
            log.info("Created IndividualCustomer CUST-003");
        }

        Customer corporate = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Demo customer CUST-001 not found"));
        Customer sme = customerRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Demo customer CUST-002 not found"));
        Customer individual = customerRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Demo customer CUST-003 not found"));

        // Current account for corporate customer
        CurrentAccount corporateCurrent = new CurrentAccount(
                "ACC-001", corporate, 1L, Currency.USD,
                new BigDecimal("10000.00"), new BigDecimal("0.50"));
        corporateCurrent.setBalance(new BigDecimal("50000.00"));
        corporateCurrent = accountRepository.save(corporateCurrent);
        createAccountHolder(corporateCurrent, corporate, AccountHolderRole.PRIMARY);
        log.info("Created CurrentAccount ACC-001 for CUST-001 with balance 50000.00");

        // Current account for SME customer
        CurrentAccount smeCurrent = new CurrentAccount(
                "ACC-002", sme, 1L, Currency.GBP,
                new BigDecimal("5000.00"), new BigDecimal("0.75"));
        smeCurrent.setBalance(new BigDecimal("25000.00"));
        smeCurrent = accountRepository.save(smeCurrent);
        createAccountHolder(smeCurrent, sme, AccountHolderRole.PRIMARY);
        log.info("Created CurrentAccount ACC-002 for CUST-002 with balance 25000.00");

        // Savings account for individual customer
        SavingsAccount individualSavings = new SavingsAccount(
                "ACC-003", individual, 2L, Currency.USD,
                new BigDecimal("500.00"), new BigDecimal("2.50"));
        individualSavings.setBalance(new BigDecimal("15000.00"));
        individualSavings = accountRepository.save(individualSavings);
        createAccountHolder(individualSavings, individual, AccountHolderRole.PRIMARY);
        log.info("Created SavingsAccount ACC-003 for CUST-003 with balance 15000.00");

        // Fixed deposit account for corporate customer
        FixedDepositAccount corporateFd = new FixedDepositAccount(
                "ACC-004", corporate, 3L, Currency.EUR,
                12, new BigDecimal("120000.00"));
        corporateFd.setBalance(new BigDecimal("100000.00"));
        corporateFd = accountRepository.save(corporateFd);
        createAccountHolder(corporateFd, corporate, AccountHolderRole.PRIMARY);
        log.info("Created FixedDepositAccount ACC-004 for CUST-001 with balance 100000.00");

        // Loan account for SME customer
        LoanAccount smeLoan = new LoanAccount(
                "ACC-005", sme, 4L, Currency.SGD,
                new BigDecimal("200000.00"), new BigDecimal("6.50"), 60);
        smeLoan.setBalance(new BigDecimal("200000.00"));
        smeLoan = accountRepository.save(smeLoan);
        createAccountHolder(smeLoan, sme, AccountHolderRole.PRIMARY);
        log.info("Created LoanAccount ACC-005 for CUST-002 with loan amount 200000.00");

        log.info("Account demo data loaded successfully - 5 accounts created.");
    }

    private void createAccountHolder(com.banking.account.domain.entity.Account account,
                                      Customer customer, AccountHolderRole role) {
        AccountHolder holder = new AccountHolder(
                account, customer, role, LocalDate.now(), AccountHolderStatus.ACTIVE);
        accountHolderRepository.save(holder);
    }
}
