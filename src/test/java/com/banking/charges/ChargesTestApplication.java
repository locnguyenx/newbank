package com.banking.charges;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.banking.charges"})
@EntityScan(basePackages = {"com.banking.charges.domain"})
@EnableJpaRepositories(basePackages = {"com.banking.charges.repository"})
public class ChargesTestApplication {
}