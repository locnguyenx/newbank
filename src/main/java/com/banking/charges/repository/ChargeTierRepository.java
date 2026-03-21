package com.banking.charges.repository;

import com.banking.charges.domain.entity.ChargeTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeTierRepository extends JpaRepository<ChargeTier, Long> {
    List<ChargeTier> findByChargeRuleId(Long chargeRuleId);
    void deleteByChargeRuleId(Long chargeRuleId);
}
