package com.banking.limits.repository;

import com.banking.limits.domain.entity.AccountLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountLimitRepository extends JpaRepository<AccountLimit, Long> {
    List<AccountLimit> findByAccountNumber(String accountNumber);
}
