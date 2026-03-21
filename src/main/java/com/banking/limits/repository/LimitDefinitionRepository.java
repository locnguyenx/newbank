package com.banking.limits.repository;

import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.enums.LimitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LimitDefinitionRepository extends JpaRepository<LimitDefinition, Long> {
    List<LimitDefinition> findByStatus(LimitStatus status);
}
