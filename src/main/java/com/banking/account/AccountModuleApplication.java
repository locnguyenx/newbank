package com.banking.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.banking.customer", "com.banking.account", "com.banking.product", "com.banking.limits", "com.banking.masterdata"})
@EntityScan(basePackages = {"com.banking.customer.domain", "com.banking.account.domain", "com.banking.product.domain", "com.banking.limits.domain", "com.banking.masterdata.domain"})
@EnableJpaRepositories(basePackages = {"com.banking.customer.repository", "com.banking.account.repository", "com.banking.product.repository", "com.banking.limits.repository", "com.banking.masterdata.repository"})
public class AccountModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountModuleApplication.class, args);
    }
}
