package com.banking.product;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.repository.ProductRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
@Order(1)
@Profile("!test")
public class ProductDemoDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductDemoDataLoader.class);

    private final ProductRepository productRepository;
    private final ProductVersionRepository productVersionRepository;

    public ProductDemoDataLoader(ProductRepository productRepository,
                                 ProductVersionRepository productVersionRepository) {
        this.productRepository = productRepository;
        this.productVersionRepository = productVersionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        loadProductData();
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
        productRepository.save(current);

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
        productRepository.save(savings);

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
        productRepository.save(fd);

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
        productRepository.save(loan);

        ProductVersion v1Loan = new ProductVersion(loan, 1, ProductStatus.ACTIVE);
        v1Loan.getAudit().setCreatedBy("system");
        v1Loan.getAudit().setUpdatedBy("system");
        v1Loan.getAudit().setCreatedAt(LocalDateTime.now());
        v1Loan.getAudit().setUpdatedAt(LocalDateTime.now());
        productVersionRepository.save(v1Loan);
        log.info("Created product: LOAN (v1 ACTIVE)");

        log.info("Product demo data loaded successfully.");
    }
}