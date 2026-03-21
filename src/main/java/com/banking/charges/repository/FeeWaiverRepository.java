package com.banking.charges.repository;

import com.banking.charges.domain.entity.FeeWaiver;
import com.banking.charges.domain.enums.WaiverScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeWaiverRepository extends JpaRepository<FeeWaiver, Long> {
    List<FeeWaiver> findByScopeAndReferenceId(WaiverScope scope, String referenceId);
    List<FeeWaiver> findByChargeDefinitionId(Long chargeDefinitionId);
    Optional<FeeWaiver> findByChargeDefinitionIdAndScopeAndReferenceId(Long chargeDefinitionId, WaiverScope scope, String referenceId);
}
