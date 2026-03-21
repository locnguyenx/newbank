package com.banking.charges.repository;

import com.banking.charges.domain.entity.ChargeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRuleRepository extends JpaRepository<ChargeRule, Long> {
    List<ChargeRule> findByChargeDefinitionId(Long chargeDefinitionId);
    void deleteByChargeDefinitionId(Long chargeDefinitionId);
}
