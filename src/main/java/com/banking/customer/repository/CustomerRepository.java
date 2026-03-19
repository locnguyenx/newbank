package com.banking.customer.repository;

import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByCustomerNumber(String customerNumber);

    Optional<Customer> findByTaxId(String taxId);

    Optional<Customer> findByStatus(CustomerStatus status);
}
