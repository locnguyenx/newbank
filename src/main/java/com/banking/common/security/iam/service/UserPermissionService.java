package com.banking.common.security.iam.service;

import com.banking.common.security.iam.dto.PermissionSummary;
import com.banking.common.security.iam.entity.RoleDefinition;
import com.banking.common.security.iam.repository.RoleDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserPermissionService {

    private final RoleDefinitionRepository roleDefinitionRepository;

    public UserPermissionService(RoleDefinitionRepository roleDefinitionRepository) {
        this.roleDefinitionRepository = roleDefinitionRepository;
    }

    public List<String> getUserPermissions(Long userId) {
        return Collections.emptyList();
    }

    public List<PermissionSummary> getUserPermissionsSummary(Long userId) {
        List<String> permissions = getUserPermissions(userId);
        
        List<PermissionSummary> parsed = permissions.stream()
                .map(this::parsePermission)
                .filter(Objects::nonNull)
                .toList();
        
        Map<String, List<String>> grouped = parsed.stream()
                .collect(Collectors.groupingBy(
                        PermissionSummary::getResource,
                        Collectors.flatMapping(ps -> ps.getActions().stream(), Collectors.toList())
                ));
        
        return grouped.entrySet().stream()
                .map(entry -> new PermissionSummary(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public boolean hasPermission(Long userId, String resource, String action) {
        List<String> permissions = getUserPermissions(userId);
        String permission = resource + ":" + action;
        return permissions.contains(permission) || permissions.contains(resource + ":*");
    }

    public boolean hasAnyPermission(Long userId, String... permissions) {
        List<String> userPermissions = getUserPermissions(userId);
        for (String permission : permissions) {
            if (userPermissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    private PermissionSummary parsePermission(String permission) {
        String[] parts = permission.split(":");
        if (parts.length == 2) {
            return new PermissionSummary(parts[0], Collections.singletonList(parts[1]));
        }
        return null;
    }
}