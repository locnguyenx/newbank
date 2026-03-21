package com.banking.limits.repository;

import com.banking.limits.domain.entity.LimitUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LimitUsageRepository extends JpaRepository<LimitUsage, Long> {

    List<LimitUsage> findByLimitDefinitionIdAndAccountNumberAndPeriodStartBetween(
            Long limitDefinitionId, String accountNumber, LocalDate start, LocalDate end);

    List<LimitUsage> findByLimitDefinitionIdAndAccountNumberAndPeriodEndGreaterThanEqual(
            Long limitDefinitionId, String accountNumber, LocalDate date);

    Optional<LimitUsage> findByLimitDefinitionIdAndAccountNumberAndPeriodStart(
            Long limitDefinitionId, String accountNumber, LocalDate periodStart);
}
