package com.banking.common.security.iam.controller;

import com.banking.common.security.iam.dto.PermissionSummary;
import com.banking.common.security.iam.service.UserPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iam/permissions")
@PreAuthorize("isAuthenticated()")
public class PermissionController {

    private final UserPermissionService permissionService;

    public PermissionController(UserPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<List<String>> getUserPermissions(@PathVariable Long userId) {
        return ResponseEntity.ok(permissionService.getUserPermissions(userId));
    }

    @GetMapping("/user/{userId}/summary")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<List<PermissionSummary>> getUserPermissionsSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(permissionService.getUserPermissionsSummary(userId));
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkPermission(
            @PathVariable Long userId,
            @RequestParam String resource,
            @RequestParam String action) {
        return ResponseEntity.ok(permissionService.hasPermission(userId, resource, action));
    }

    @GetMapping
    public ResponseEntity<List<PermissionSummary>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }
}