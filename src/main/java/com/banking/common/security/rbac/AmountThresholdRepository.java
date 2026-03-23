package com.banking.common.security.rbac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AmountThresholdRepository extends JpaRepository<AmountThreshold, Long> {
    List<AmountThreshold> findByUserId(Long userId);
    Optional<AmountThreshold> findByUserIdAndRole(Long userId, Role role);
}