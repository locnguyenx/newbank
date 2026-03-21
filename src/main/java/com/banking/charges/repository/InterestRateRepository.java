package com.banking.charges.repository;

import com.banking.charges.domain.entity.InterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRateRepository extends JpaRepository<InterestRate, Long> {
    List<InterestRate> findByProductCode(String productCode);
    Optional<InterestRate> findByChargeDefinitionIdAndProductCode(Long chargeDefinitionId, String productCode);
}
