package com.banking.common.security.rbac;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionEvaluatorTest {

    @Mock
    private UserScopeRepository userScopeRepository;

    @Mock
    private AmountThresholdRepository amountThresholdRepository;

    @InjectMocks
    private PermissionEvaluator permissionEvaluator;

    @BeforeEach
    void setUp() {
    }

    @Test
    void companyMakerCanCreateButNotApprove() {
        Long userId = 1L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.COMPANY, 100L, Role.COMPANY_MAKER))
        );

        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "account", "create"));
        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertFalse(permissionEvaluator.hasPermission(userId, "account", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
        assertTrue(permissionEvaluator.hasPermission(userId, "account", "view"));
    }

    @Test
    void companyCheckerCanApproveButNotCreate() {
        Long userId = 2L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.COMPANY, 100L, Role.COMPANY_CHECKER))
        );

        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertFalse(permissionEvaluator.hasPermission(userId, "account", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "account", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
        assertTrue(permissionEvaluator.hasPermission(userId, "account", "view"));
    }

    @Test
    void viewerCanOnlyRead() {
        Long userId = 3L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.COMPANY, 100L, Role.COMPANY_VIEWER))
        );

        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
        assertFalse(permissionEvaluator.hasPermission(userId, "account", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "account", "view"));
    }

    @Test
    void adminHasAllPermissions() {
        Long userId = 4L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.GLOBAL, null, Role.SYSTEM_ADMIN))
        );

        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
        assertTrue(permissionEvaluator.hasPermission(userId, "account", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "account", "view"));
    }

    @Test
    void departmentMakerCanCreateButNotApprove() {
        Long userId = 5L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.DEPARTMENT, 200L, Role.DEPARTMENT_MAKER))
        );

        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
    }

    @Test
    void departmentCheckerCanApproveButNotCreate() {
        Long userId = 6L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.DEPARTMENT, 200L, Role.DEPARTMENT_CHECKER))
        );

        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
    }

    @Test
    void departmentViewerCanOnlyRead() {
        Long userId = 7L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.DEPARTMENT, 200L, Role.DEPARTMENT_VIEWER))
        );

        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
    }

    @Test
    void hoAdminHasAllPermissions() {
        Long userId = 8L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.GLOBAL, null, Role.HO_ADMIN))
        );

        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
    }

    @Test
    void branchAdminHasAllPermissions() {
        Long userId = 9L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.BRANCH, 300L, Role.BRANCH_ADMIN))
        );

        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
    }

    @Test
    void companyAdminHasAllPermissions() {
        Long userId = 10L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(
                List.of(new UserScope(userId, ScopeType.COMPANY, 100L, Role.COMPANY_ADMIN))
        );

        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertTrue(permissionEvaluator.hasPermission(userId, "payment", "view"));
    }

    @Test
    void userWithNoScopesHasNoPermissions() {
        Long userId = 11L;
        when(userScopeRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "create"));
        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "approve"));
        assertFalse(permissionEvaluator.hasPermission(userId, "payment", "view"));
    }

    @Test
    void hasApprovalAuthorityWithinThreshold() {
        Long userId = 12L;
        BigDecimal threshold = new BigDecimal("10000.0000");
        BigDecimal amount = new BigDecimal("5000.0000");

        when(amountThresholdRepository.findByUserId(userId)).thenReturn(
                List.of(new AmountThreshold(userId, Role.COMPANY_CHECKER, threshold))
        );

        assertTrue(permissionEvaluator.hasApprovalAuthority(userId, amount));
    }

    @Test
    void hasApprovalAuthorityAboveThreshold() {
        Long userId = 13L;
        BigDecimal threshold = new BigDecimal("10000.0000");
        BigDecimal amount = new BigDecimal("15000.0000");

        when(amountThresholdRepository.findByUserId(userId)).thenReturn(
                List.of(new AmountThreshold(userId, Role.COMPANY_CHECKER, threshold))
        );

        assertFalse(permissionEvaluator.hasApprovalAuthority(userId, amount));
    }

    @Test
    void hasApprovalAuthorityNoThreshold() {
        Long userId = 14L;
        BigDecimal amount = new BigDecimal("5000.0000");

        when(amountThresholdRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        assertFalse(permissionEvaluator.hasApprovalAuthority(userId, amount));
    }

    @Test
    void approvalAuthorityReturnsMaxThresholdWhenMultipleRoles() {
        Long userId = 15L;
        BigDecimal amount = new BigDecimal("15000.0000");

        when(amountThresholdRepository.findByUserId(userId)).thenReturn(
                List.of(
                        new AmountThreshold(userId, Role.COMPANY_VIEWER, new BigDecimal("1000.0000")),
                        new AmountThreshold(userId, Role.COMPANY_CHECKER, new BigDecimal("20000.0000"))
                )
        );

        assertTrue(permissionEvaluator.hasApprovalAuthority(userId, amount));
    }
}