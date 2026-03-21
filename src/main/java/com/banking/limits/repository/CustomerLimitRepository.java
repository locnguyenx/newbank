package com.banking.limits.repository;

import com.banking.limits.domain.entity.CustomerLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerLimitRepository extends JpaRepository<CustomerLimit, Long> {
    List<CustomerLimit> findByCustomerId(Long customerId);
}
