package com.banking.masterdata;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.banking.masterdata"})
@EntityScan(basePackages = {"com.banking.masterdata.domain"})
@EnableJpaRepositories(basePackages = {"com.banking.masterdata.repository"})
public class MasterDataTestApplication {
}
