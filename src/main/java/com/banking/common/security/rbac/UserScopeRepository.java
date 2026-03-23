package com.banking.common.security.rbac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserScopeRepository extends JpaRepository<UserScope, Long> {
    List<UserScope> findByUserId(Long userId);
    List<UserScope> findByUserIdAndScopeType(Long userId, ScopeType scopeType);
    List<UserScope> findByUserIdAndScopeTypeAndScopeId(Long userId, ScopeType scopeType, Long scopeId);
    boolean existsByUserIdAndRole(Long userId, Role role);
}