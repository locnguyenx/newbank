package com.banking.customer;

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
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(1)
@Profile("!test")
public class CustomerDemoDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CustomerDemoDataLoader.class);

    private final CustomerRepository customerRepository;

    public CustomerDemoDataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            log.info("Demo customers already exist, skipping.");
            return;
        }

        log.info("Creating demo customers...");
        Customer corporate = new CorporateCustomer("CUST-001", "Acme Corporation", CustomerStatus.ACTIVE);
        customerRepository.save(corporate);
        log.info("Created CorporateCustomer CUST-001");

        Customer sme = new SMECustomer("CUST-002", "Joe's Bakery Ltd", CustomerStatus.ACTIVE);
        customerRepository.save(sme);
        log.info("Created SMECustomer CUST-002");

        Customer individual = new IndividualCustomer("CUST-003", "Jane Smith", CustomerStatus.ACTIVE);
        customerRepository.save(individual);
        log.info("Created IndividualCustomer CUST-003");
    }
}
