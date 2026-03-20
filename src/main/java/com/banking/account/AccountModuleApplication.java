package com.banking.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.banking.customer.domain", "com.banking.account.domain"})
@EnableJpaRepositories(basePackages = {"com.banking.customer.repository", "com.banking.account.repository"})
public class AccountModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountModuleApplication.class, args);
    }
}
