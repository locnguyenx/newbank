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
import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.repository.ProductRepository;
import com.banking.product.repository.ProductVersionRepository;
import com.banking.product.service.ProductQueryService;
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
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductVersionRepository productVersionRepository;
    private final ProductQueryService productQueryService;

    public DemoDataLoader(AccountRepository accountRepository,
                          AccountHolderRepository accountHolderRepository,
                          CustomerRepository customerRepository,
                          ProductRepository productRepository,
                          ProductVersionRepository productVersionRepository,
                          ProductQueryService productQueryService) {
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.productVersionRepository = productVersionRepository;
        this.productQueryService = productQueryService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadProductData();
        loadAccountData();
    }

    private void loadProductData() {
        if (productRepository.count() > 0) {
            log.info("Product demo data already exists, skipping.");
            return;
        }

        log.info("Loading product demo data...");

        Product current = new Product("CURRENT", "Current Account", ProductFamily.ACCOUNT, "Standard current account");
        current.getAudit().setCreatedBy("system");
        current.getAudit().setUpdatedBy("system");
        current.getAudit().setCreatedAt(LocalDateTime.now());
        current.getAudit().setUpdatedAt(LocalDateTime.now());
        current = productRepository.save(current);

        ProductVersion v1Current = new ProductVersion(current, 1, ProductStatus.ACTIVE);
        v1Current.getAudit().setCreatedBy("system");
        v1Current.getAudit().setUpdatedBy("system");
        v1Current.getAudit().setCreatedAt(LocalDateTime.now());
        v1Current.getAudit().setUpdatedAt(LocalDateTime.now());
        productVersionRepository.save(v1Current);
        log.info("Created product: CURRENT (v1 ACTIVE)");

        Product savings = new Product("SAVINGS", "Savings Account", ProductFamily.ACCOUNT, "Interest-bearing savings account");
        savings.getAudit().setCreatedBy("system");
        savings.getAudit().setUpdatedBy("system");
        savings.getAudit().setCreatedAt(LocalDateTime.now());
        savings.getAudit().setUpdatedAt(LocalDateTime.now());
        savings = productRepository.save(savings);

        ProductVersion v1Savings = new ProductVersion(savings, 1, ProductStatus.ACTIVE);
        v1Savings.getAudit().setCreatedBy("system");
        v1Savings.getAudit().setUpdatedBy("system");
        v1Savings.getAudit().setCreatedAt(LocalDateTime.now());
        v1Savings.getAudit().setUpdatedAt(LocalDateTime.now());
        productVersionRepository.save(v1Savings);
        log.info("Created product: SAVINGS (v1 ACTIVE)");

        Product fd = new Product("FIXED-DEPOSIT", "Fixed Deposit", ProductFamily.ACCOUNT, "Term deposit with fixed interest rate");
        fd.getAudit().setCreatedBy("system");
        fd.getAudit().setUpdatedBy("system");
        fd.getAudit().setCreatedAt(LocalDateTime.now());
        fd.getAudit().setUpdatedAt(LocalDateTime.now());
        fd = productRepository.save(fd);

        ProductVersion v1Fd = new ProductVersion(fd, 1, ProductStatus.ACTIVE);
        v1Fd.getAudit().setCreatedBy("system");
        v1Fd.getAudit().setUpdatedBy("system");
        v1Fd.getAudit().setCreatedAt(LocalDateTime.now());
        v1Fd.getAudit().setUpdatedAt(LocalDateTime.now());
        productVersionRepository.save(v1Fd);
        log.info("Created product: FIXED-DEPOSIT (v1 ACTIVE)");

        Product loan = new Product("LOAN", "Loan Account", ProductFamily.ACCOUNT, "Business loan account");
        loan.getAudit().setCreatedBy("system");
        loan.getAudit().setUpdatedBy("system");
        loan.getAudit().setCreatedAt(LocalDateTime.now());
        loan.getAudit().setUpdatedAt(LocalDateTime.now());
        loan = productRepository.save(loan);

        ProductVersion v1Loan = new ProductVersion(loan, 1, ProductStatus.ACTIVE);
        v1Loan.getAudit().setCreatedBy("system");
        v1Loan.getAudit().setUpdatedBy("system");
        v1Loan.getAudit().setCreatedAt(LocalDateTime.now());
        v1Loan.getAudit().setUpdatedAt(LocalDateTime.now());
        productVersionRepository.save(v1Loan);
        log.info("Created product: LOAN (v1 ACTIVE)");

        log.info("Product demo data loaded successfully.");
    }

    private void loadAccountData() {
        if (accountRepository.count() > 0) {
            log.info("Account demo data already exists, skipping.");
            return;
        }

        log.info("Loading account demo data...");

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

        var pvCurrent = productQueryService.getActiveProductByCode("CURRENT").orElseThrow();
        var pvSavings = productQueryService.getActiveProductByCode("SAVINGS").orElseThrow();
        var pvFd = productQueryService.getActiveProductByCode("FIXED-DEPOSIT").orElseThrow();
        var pvLoan = productQueryService.getActiveProductByCode("LOAN").orElseThrow();

        CurrentAccount corporateCurrent = new CurrentAccount(
                "ACC-001", corporate, pvCurrent.getProductId(), Currency.USD,
                new BigDecimal("10000.00"), new BigDecimal("0.50"));
        corporateCurrent.setProductVersionId(pvCurrent.getId());
        corporateCurrent.setProductName(pvCurrent.getProductName());
        corporateCurrent.setBalance(new BigDecimal("50000.00"));
        corporateCurrent = accountRepository.save(corporateCurrent);
        createAccountHolder(corporateCurrent, corporate, AccountHolderRole.PRIMARY);
        log.info("Created CurrentAccount ACC-001 for CUST-001 with balance 50000.00");

        CurrentAccount smeCurrent = new CurrentAccount(
                "ACC-002", sme, pvCurrent.getProductId(), Currency.GBP,
                new BigDecimal("5000.00"), new BigDecimal("0.75"));
        smeCurrent.setProductVersionId(pvCurrent.getId());
        smeCurrent.setProductName(pvCurrent.getProductName());
        smeCurrent.setBalance(new BigDecimal("25000.00"));
        smeCurrent = accountRepository.save(smeCurrent);
        createAccountHolder(smeCurrent, sme, AccountHolderRole.PRIMARY);
        log.info("Created CurrentAccount ACC-002 for CUST-002 with balance 25000.00");

        SavingsAccount individualSavings = new SavingsAccount(
                "ACC-003", individual, pvSavings.getProductId(), Currency.USD,
                new BigDecimal("500.00"), new BigDecimal("2.50"));
        individualSavings.setProductVersionId(pvSavings.getId());
        individualSavings.setProductName(pvSavings.getProductName());
        individualSavings.setBalance(new BigDecimal("15000.00"));
        individualSavings = accountRepository.save(individualSavings);
        createAccountHolder(individualSavings, individual, AccountHolderRole.PRIMARY);
        log.info("Created SavingsAccount ACC-003 for CUST-003 with balance 15000.00");

        FixedDepositAccount corporateFd = new FixedDepositAccount(
                "ACC-004", corporate, pvFd.getProductId(), Currency.EUR,
                12, new BigDecimal("120000.00"));
        corporateFd.setProductVersionId(pvFd.getId());
        corporateFd.setProductName(pvFd.getProductName());
        corporateFd.setBalance(new BigDecimal("100000.00"));
        corporateFd = accountRepository.save(corporateFd);
        createAccountHolder(corporateFd, corporate, AccountHolderRole.PRIMARY);
        log.info("Created FixedDepositAccount ACC-004 for CUST-001 with balance 100000.00");

        LoanAccount smeLoan = new LoanAccount(
                "ACC-005", sme, pvLoan.getProductId(), Currency.SGD,
                new BigDecimal("200000.00"), new BigDecimal("6.50"), 60);
        smeLoan.setProductVersionId(pvLoan.getId());
        smeLoan.setProductName(pvLoan.getProductName());
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
