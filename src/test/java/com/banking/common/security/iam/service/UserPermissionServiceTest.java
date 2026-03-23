package com.banking.common.security.iam.service;

import com.banking.common.security.iam.dto.PermissionSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPermissionServiceTest {

    @Mock
    private com.banking.common.security.iam.repository.RoleDefinitionRepository roleDefinitionRepository;

    @InjectMocks
    private UserPermissionService userPermissionService;

    @Test
    void getUserPermissions_ReturnsEmptyList() {
        List<String> permissions = userPermissionService.getUserPermissions(1L);
        
        assertNotNull(permissions);
        assertTrue(permissions.isEmpty());
    }

    @Test
    void getUserPermissionsSummary_ReturnsEmptyList() {
        List<PermissionSummary> summary = userPermissionService.getUserPermissionsSummary(1L);
        
        assertNotNull(summary);
        assertTrue(summary.isEmpty());
    }

    @Test
    void hasPermission_ReturnsFalse() {
        boolean result = userPermissionService.hasPermission(1L, "users", "read");
        
        assertFalse(result);
    }

    @Test
    void hasAnyPermission_ReturnsFalse() {
        boolean result = userPermissionService.hasAnyPermission(1L, "users:read", "users:write");
        
        assertFalse(result);
    }
}