package com.banking.common.security.iam.service;

import com.banking.common.security.iam.dto.RoleRequest;
import com.banking.common.security.iam.dto.RoleResponse;
import com.banking.common.security.iam.entity.RoleDefinition;
import com.banking.common.security.iam.exception.RoleNotFoundException;
import com.banking.common.security.iam.exception.RoleOperationDeniedException;
import com.banking.common.security.iam.repository.RoleDefinitionRepository;
import com.banking.common.security.iam.repository.RolePermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleManagementService {

    private final RoleDefinitionRepository roleDefinitionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleManagementService(RoleDefinitionRepository roleDefinitionRepository,
                                   RolePermissionRepository rolePermissionRepository) {
        this.roleDefinitionRepository = roleDefinitionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public RoleResponse createRole(RoleRequest request) {
        if (roleDefinitionRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Role with this name already exists");
        }
        
        RoleDefinition role = new RoleDefinition();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setSystem(false);
        
        if (request.getPermissions() != null) {
            role.setPermissions(request.getPermissions());
        }
        
        role = roleDefinitionRepository.save(role);
        return toResponse(role);
    }

    public RoleResponse updateRole(Long id, RoleRequest request) {
        RoleDefinition role = roleDefinitionRepository.findById(id)
                .orElseThrow(() -> RoleNotFoundException.notFound(id));
        
        if (role.isSystem()) {
            throw RoleOperationDeniedException.cannotModifySystemRole();
        }
        
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
        if (request.getPermissions() != null) {
            role.setPermissions(request.getPermissions());
        }
        
        role = roleDefinitionRepository.save(role);
        return toResponse(role);
    }

    @Transactional(readOnly = true)
    public RoleResponse getRole(Long id) {
        RoleDefinition role = roleDefinitionRepository.findById(id)
                .orElseThrow(() -> RoleNotFoundException.notFound(id));
        return toResponse(role);
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> listRoles() {
        return roleDefinitionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> listCustomRoles() {
        return roleDefinitionRepository.findByIsSystem(false).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteRole(Long id) {
        RoleDefinition role = roleDefinitionRepository.findById(id)
                .orElseThrow(() -> RoleNotFoundException.notFound(id));
        
        if (role.isSystem()) {
            throw RoleOperationDeniedException.cannotDeleteSystemRole();
        }
        
        rolePermissionRepository.deleteByRoleId(id);
        roleDefinitionRepository.delete(role);
    }

    public RoleResponse assignPermissions(Long id, List<String> permissions) {
        RoleDefinition role = roleDefinitionRepository.findById(id)
                .orElseThrow(() -> RoleNotFoundException.notFound(id));
        
        if (role.isSystem()) {
            throw RoleOperationDeniedException.cannotModifySystemRole();
        }
        
        role.setPermissions(permissions);
        role = roleDefinitionRepository.save(role);
        return toResponse(role);
    }

    private RoleResponse toResponse(RoleDefinition role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setSystem(role.isSystem());
        response.setPermissions(role.getPermissions());
        response.setCreatedAt(role.getAuditFields().getCreatedAt());
        response.setUpdatedAt(role.getAuditFields().getUpdatedAt());
        return response;
    }
}