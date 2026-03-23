package com.banking.common.security.iam.service;

import com.banking.common.security.iam.dto.RoleRequest;
import com.banking.common.security.iam.dto.RoleResponse;
import com.banking.common.security.iam.entity.RoleDefinition;
import com.banking.common.security.iam.exception.RoleNotFoundException;
import com.banking.common.security.iam.exception.RoleOperationDeniedException;
import com.banking.common.security.iam.repository.RoleDefinitionRepository;
import com.banking.common.security.iam.repository.RolePermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleManagementServiceTest {

    @Mock
    private RoleDefinitionRepository roleDefinitionRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private RoleManagementService roleManagementService;

    private RoleRequest createRoleRequest;
    private RoleDefinition systemRole;
    private RoleDefinition customRole;

    @BeforeEach
    void setUp() {
        createRoleRequest = new RoleRequest();
        createRoleRequest.setName("TEST_ROLE");
        createRoleRequest.setDescription("Test Role");
        createRoleRequest.setPermissions(Arrays.asList("users:read", "users:write"));

        systemRole = new RoleDefinition("SYSTEM_ADMIN", "System Admin", true);
        systemRole.setId(1L);

        customRole = new RoleDefinition("CUSTOM_ROLE", "Custom Role", false);
        customRole.setId(2L);
    }

    @Test
    void createRole_Success() {
        when(roleDefinitionRepository.existsByName("TEST_ROLE")).thenReturn(false);
        when(roleDefinitionRepository.save(any(RoleDefinition.class))).thenAnswer(invocation -> {
            RoleDefinition role = invocation.getArgument(0);
            role.setId(1L);
            return role;
        });

        RoleResponse response = roleManagementService.createRole(createRoleRequest);

        assertNotNull(response);
        assertEquals("TEST_ROLE", response.getName());
        verify(roleDefinitionRepository).save(any(RoleDefinition.class));
    }

    @Test
    void createRole_ThrowsWhenNameExists() {
        when(roleDefinitionRepository.existsByName("TEST_ROLE")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            roleManagementService.createRole(createRoleRequest));
    }

    @Test
    void getRole_Success() {
        when(roleDefinitionRepository.findById(1L)).thenReturn(Optional.of(systemRole));

        RoleResponse response = roleManagementService.getRole(1L);

        assertNotNull(response);
        assertEquals("SYSTEM_ADMIN", response.getName());
        assertTrue(response.isSystem());
    }

    @Test
    void getRole_ThrowsWhenNotFound() {
        when(roleDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> 
            roleManagementService.getRole(999L));
    }

    @Test
    void listRoles_Success() {
        when(roleDefinitionRepository.findAll()).thenReturn(Arrays.asList(systemRole, customRole));

        List<RoleResponse> roles = roleManagementService.listRoles();

        assertEquals(2, roles.size());
    }

    @Test
    void deleteRole_Success() {
        when(roleDefinitionRepository.findById(2L)).thenReturn(Optional.of(customRole));
        doNothing().when(rolePermissionRepository).deleteByRoleId(2L);
        doNothing().when(roleDefinitionRepository).delete(customRole);

        roleManagementService.deleteRole(2L);

        verify(roleDefinitionRepository).delete(customRole);
    }

    @Test
    void deleteRole_ThrowsWhenSystemRole() {
        when(roleDefinitionRepository.findById(1L)).thenReturn(Optional.of(systemRole));

        assertThrows(RoleOperationDeniedException.class, () -> 
            roleManagementService.deleteRole(1L));
    }

    @Test
    void updateRole_Success() {
        RoleRequest updateRequest = new RoleRequest();
        updateRequest.setDescription("Updated Description");
        updateRequest.setPermissions(Arrays.asList("users:read"));

        when(roleDefinitionRepository.findById(2L)).thenReturn(Optional.of(customRole));
        when(roleDefinitionRepository.save(any(RoleDefinition.class))).thenReturn(customRole);

        RoleResponse response = roleManagementService.updateRole(2L, updateRequest);

        assertNotNull(response);
        verify(roleDefinitionRepository).save(customRole);
    }

    @Test
    void updateRole_ThrowsWhenSystemRole() {
        RoleRequest updateRequest = new RoleRequest();
        updateRequest.setDescription("Updated Description");

        when(roleDefinitionRepository.findById(1L)).thenReturn(Optional.of(systemRole));

        assertThrows(RoleOperationDeniedException.class, () -> 
            roleManagementService.updateRole(1L, updateRequest));
    }

    @Test
    void assignPermissions_Success() {
        List<String> permissions = Arrays.asList("users:read", "users:write", "users:delete");
        
        when(roleDefinitionRepository.findById(2L)).thenReturn(Optional.of(customRole));
        when(roleDefinitionRepository.save(any(RoleDefinition.class))).thenReturn(customRole);

        RoleResponse response = roleManagementService.assignPermissions(2L, permissions);

        assertNotNull(response);
        verify(roleDefinitionRepository).save(customRole);
    }

    @Test
    void assignPermissions_ThrowsWhenSystemRole() {
        List<String> permissions = Arrays.asList("users:read");
        
        when(roleDefinitionRepository.findById(1L)).thenReturn(Optional.of(systemRole));

        assertThrows(RoleOperationDeniedException.class, () -> 
            roleManagementService.assignPermissions(1L, permissions));
    }
}