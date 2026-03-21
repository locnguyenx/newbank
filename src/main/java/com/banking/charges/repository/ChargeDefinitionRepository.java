package com.banking.charges.repository;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChargeDefinitionRepository extends JpaRepository<ChargeDefinition, Long> {
    Optional<ChargeDefinition> findByName(String name);
    List<ChargeDefinition> findByStatus(ChargeStatus status);
    List<ChargeDefinition> findByChargeType(ChargeType chargeType);
    List<ChargeDefinition> findByChargeTypeAndStatus(ChargeType chargeType, ChargeStatus status);
    boolean existsByName(String name);
}