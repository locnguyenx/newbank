package com.banking.limits.repository;

import com.banking.limits.domain.entity.ApprovalThreshold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalThresholdRepository extends JpaRepository<ApprovalThreshold, Long> {
    Optional<ApprovalThreshold> findByLimitDefinitionId(Long limitDefinitionId);
}
