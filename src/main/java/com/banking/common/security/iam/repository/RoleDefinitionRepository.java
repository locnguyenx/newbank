package com.banking.common.security.iam.repository;

import com.banking.common.security.iam.entity.RoleDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleDefinitionRepository extends JpaRepository<RoleDefinition, Long> {
    Optional<RoleDefinition> findByName(String name);
    boolean existsByName(String name);
    List<RoleDefinition> findByIsSystem(boolean isSystem);
}