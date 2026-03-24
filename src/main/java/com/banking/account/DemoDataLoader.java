package com.banking.account;

import com.banking.account.domain.entity.AccountHolder;
import com.banking.account.domain.entity.CurrentAccount;
import com.banking.account.domain.entity.FixedDepositAccount;
import com.banking.account.domain.entity.LoanAccount;
import com.banking.account.domain.entity.SavingsAccount;
import com.banking.account.domain.enums.AccountHolderRole;
import com.banking.account.domain.enums.AccountHolderStatus;
import com.banking.account.repository.AccountHolderRepository;
import com.banking.account.repository.AccountRepository;
import com.banking.customer.api.CustomerQueryService;
import com.banking.customer.api.dto.CustomerDTO;
import com.banking.product.api.ProductQueryService;
import com.banking.product.api.dto.ProductVersionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Order(2)
@Profile("!test")
public class DemoDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataLoader.class);

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final CustomerQueryService customerQueryService;
    private final ProductQueryService productQueryService;

    public DemoDataLoader(AccountRepository accountRepository,
                          AccountHolderRepository accountHolderRepository,
                          CustomerQueryService customerQueryService,
                          ProductQueryService productQueryService) {
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.customerQueryService = customerQueryService;
        this.productQueryService = productQueryService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadAccountData();
    }

    private void loadAccountData() {
        if (accountRepository.count() > 0) {
            log.info("Account demo data already exists, skipping.");
            return;
        }

        log.info("Loading account demo data...");

        CustomerDTO corporate = customerQueryService.findById(1L);
        if (corporate == null) throw new RuntimeException("Demo customer CUST-001 not found. Ensure CustomerDemoDataLoader runs first.");
        Long corporateId = corporate.getId();
        CustomerDTO sme = customerQueryService.findById(2L);
        if (sme == null) throw new RuntimeException("Demo customer CUST-002 not found. Ensure CustomerDemoDataLoader runs first.");
        Long smeId = sme.getId();
        CustomerDTO individual = customerQueryService.findById(3L);
        if (individual == null) throw new RuntimeException("Demo customer CUST-003 not found. Ensure CustomerDemoDataLoader runs first.");
        Long individualId = individual.getId();

        var pvCurrent = productQueryService.findActiveVersionByCode("CURRENT").orElseThrow();
        var pvSavings = productQueryService.findActiveVersionByCode("SAVINGS").orElseThrow();
        var pvFd = productQueryService.findActiveVersionByCode("FIXED-DEPOSIT").orElseThrow();
        var pvLoan = productQueryService.findActiveVersionByCode("LOAN").orElseThrow();

        CurrentAccount corporateCurrent = new CurrentAccount(
                "ACC-001", corporateId, pvCurrent.getProductId(), "USD",
                new BigDecimal("10000.00"), new BigDecimal("0.50"));
        corporateCurrent.setProductVersionId(pvCurrent.getId());
        corporateCurrent.setProductName(pvCurrent.getProductName());
        corporateCurrent.setBalance(new BigDecimal("50000.00"));
        corporateCurrent = accountRepository.save(corporateCurrent);
        createAccountHolder(corporateCurrent, corporateId, AccountHolderRole.PRIMARY);
        log.info("Created CurrentAccount ACC-001 for CUST-001 with balance 50000.00");

        CurrentAccount smeCurrent = new CurrentAccount(
                "ACC-002", smeId, pvCurrent.getProductId(), "GBP",
                new BigDecimal("5000.00"), new BigDecimal("0.75"));
        smeCurrent.setProductVersionId(pvCurrent.getId());
        smeCurrent.setProductName(pvCurrent.getProductName());
        smeCurrent.setBalance(new BigDecimal("25000.00"));
        smeCurrent = accountRepository.save(smeCurrent);
        createAccountHolder(smeCurrent, smeId, AccountHolderRole.PRIMARY);
        log.info("Created CurrentAccount ACC-002 for CUST-002 with balance 25000.00");

        SavingsAccount individualSavings = new SavingsAccount(
                "ACC-003", individualId, pvSavings.getProductId(), "USD",
                new BigDecimal("500.00"), new BigDecimal("2.50"));
        individualSavings.setProductVersionId(pvSavings.getId());
        individualSavings.setProductName(pvSavings.getProductName());
        individualSavings.setBalance(new BigDecimal("15000.00"));
        individualSavings = accountRepository.save(individualSavings);
        createAccountHolder(individualSavings, individualId, AccountHolderRole.PRIMARY);
        log.info("Created SavingsAccount ACC-003 for CUST-003 with balance 15000.00");

        FixedDepositAccount corporateFd = new FixedDepositAccount(
                "ACC-004", corporateId, pvFd.getProductId(), "EUR",
                12, new BigDecimal("120000.00"));
        corporateFd.setProductVersionId(pvFd.getId());
        corporateFd.setProductName(pvFd.getProductName());
        corporateFd.setBalance(new BigDecimal("100000.00"));
        corporateFd = accountRepository.save(corporateFd);
        createAccountHolder(corporateFd, corporateId, AccountHolderRole.PRIMARY);
        log.info("Created FixedDepositAccount ACC-004 for CUST-001 with balance 100000.00");

        LoanAccount smeLoan = new LoanAccount(
                "ACC-005", smeId, pvLoan.getProductId(), "SGD",
                new BigDecimal("200000.00"), new BigDecimal("6.50"), 60);
        smeLoan.setProductVersionId(pvLoan.getId());
        smeLoan.setProductName(pvLoan.getProductName());
        smeLoan.setBalance(new BigDecimal("200000.00"));
        smeLoan = accountRepository.save(smeLoan);
        createAccountHolder(smeLoan, smeId, AccountHolderRole.PRIMARY);
        log.info("Created LoanAccount ACC-005 for CUST-002 with loan amount 200000.00");

        log.info("Account demo data loaded successfully - 5 accounts created.");
    }

    private void createAccountHolder(com.banking.account.domain.entity.Account account,
                                   Long customerId, AccountHolderRole role) {
        AccountHolder holder = new AccountHolder(
                account, customerId, role, LocalDate.now(), AccountHolderStatus.ACTIVE);
        accountHolderRepository.save(holder);
    }
}