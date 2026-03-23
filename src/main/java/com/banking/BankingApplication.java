package com.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {
    "com.banking.common",
    "com.banking.customer",
    "com.banking.masterdata",
    "com.banking.account",
    "com.banking.product",
    "com.banking.limits",
    "com.banking.charges"
})
@EntityScan(basePackages = {
    "com.banking.common.security.entity",
    "com.banking.common.security.iam.entity",
    "com.banking.common.security.mfa",
    "com.banking.common.security.rbac",
    "com.banking.common.audit",
    "com.banking.customer.domain",
    "com.banking.masterdata.domain",
    "com.banking.account.domain",
    "com.banking.product.domain",
    "com.banking.limits.domain",
    "com.banking.charges.domain"
})
@EnableJpaRepositories(basePackages = {
    "com.banking.common.security.entity",
    "com.banking.common.audit",
    "com.banking.common.security.iam",
    "com.banking.common.security.mfa",
    "com.banking.common.security.rbac",
    "com.banking.customer.repository",
    "com.banking.masterdata.repository",
    "com.banking.account.repository",
    "com.banking.product.repository",
    "com.banking.limits.repository",
    "com.banking.charges.repository"
})
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
}
