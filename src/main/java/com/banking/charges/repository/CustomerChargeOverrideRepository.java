package com.banking.charges.repository;

import com.banking.charges.domain.entity.CustomerChargeOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerChargeOverrideRepository extends JpaRepository<CustomerChargeOverride, Long> {
    List<CustomerChargeOverride> findByCustomerId(Long customerId);
    Optional<CustomerChargeOverride> findByChargeDefinitionIdAndCustomerId(Long chargeDefinitionId, Long customerId);
    void deleteByChargeDefinitionIdAndCustomerId(Long chargeDefinitionId, Long customerId);
}
