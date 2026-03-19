package com.banking.customer.repository;

import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.enums.KYCStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface KYCCheckRepository extends JpaRepository<KYCCheck, Long> {

    List<KYCCheck> findByCustomerAndStatus(Customer customer, KYCStatus status);

    List<KYCCheck> findByDueDateBefore(Instant date);
}