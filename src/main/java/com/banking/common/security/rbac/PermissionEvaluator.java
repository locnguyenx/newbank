package com.banking.common.security.rbac;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PermissionEvaluator {

    private final UserScopeRepository userScopeRepository;
    private final AmountThresholdRepository amountThresholdRepository;

    private static final Map<Role, Set<String>> PERMISSION_MAP = Map.ofEntries(
            Map.entry(Role.SYSTEM_ADMIN, Set.of("payment:create", "payment:approve", "payment:view", "account:create", "account:approve", "account:view")),
            Map.entry(Role.HO_ADMIN, Set.of("payment:create", "payment:approve", "payment:view", "account:create", "account:approve", "account:view")),
            Map.entry(Role.BRANCH_ADMIN, Set.of("payment:create", "payment:approve", "payment:view", "account:create", "account:approve", "account:view")),
            Map.entry(Role.DEPARTMENT_MAKER, Set.of("payment:create", "payment:view", "account:create", "account:view")),
            Map.entry(Role.DEPARTMENT_CHECKER, Set.of("payment:approve", "payment:view", "account:approve", "account:view")),
            Map.entry(Role.DEPARTMENT_VIEWER, Set.of("payment:view", "account:view")),
            Map.entry(Role.COMPANY_ADMIN, Set.of("payment:create", "payment:approve", "payment:view", "account:create", "account:approve", "account:view")),
            Map.entry(Role.COMPANY_MAKER, Set.of("payment:create", "payment:view", "account:create", "account:view")),
            Map.entry(Role.COMPANY_CHECKER, Set.of("payment:approve", "payment:view", "account:approve", "account:view")),
            Map.entry(Role.COMPANY_VIEWER, Set.of("payment:view", "account:view"))
    );

    public PermissionEvaluator(UserScopeRepository userScopeRepository, AmountThresholdRepository amountThresholdRepository) {
        this.userScopeRepository = userScopeRepository;
        this.amountThresholdRepository = amountThresholdRepository;
    }

    public boolean hasPermission(Long userId, String resource, String action) {
        List<UserScope> userScopes = userScopeRepository.findByUserId(userId);
        if (userScopes.isEmpty()) {
            return false;
        }

        String permission = resource + ":" + action;
        for (UserScope userScope : userScopes) {
            Role role = userScope.getRole();
            Set<String> permissions = PERMISSION_MAP.get(role);
            if (permissions != null && permissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasApprovalAuthority(Long userId, BigDecimal amount) {
        List<AmountThreshold> thresholds = amountThresholdRepository.findByUserId(userId);
        if (thresholds.isEmpty()) {
            return false;
        }

        BigDecimal maxThreshold = BigDecimal.ZERO;
        for (AmountThreshold threshold : thresholds) {
            if (threshold.getThreshold().compareTo(maxThreshold) > 0) {
                maxThreshold = threshold.getThreshold();
            }
        }

        return amount.compareTo(maxThreshold) <= 0;
    }
}