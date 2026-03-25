package com.banking.common.security.iam.controller;

import com.banking.common.security.iam.dto.LoginHistoryResponse;
import com.banking.common.security.iam.entity.FailedLoginAttempt;
import com.banking.common.security.iam.service.ActivityMonitoringService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iam/activity")
@PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'COMPANY_ADMIN')")
public class ActivityController {

    private final ActivityMonitoringService activityMonitoringService;

    public ActivityController(ActivityMonitoringService activityMonitoringService) {
        this.activityMonitoringService = activityMonitoringService;
    }

    @GetMapping("/login-history/user/{userId}")
    public ResponseEntity<List<LoginHistoryResponse>> getUserLoginHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(activityMonitoringService.getUserLoginHistory(userId));
    }

    @GetMapping("/login-history")
    public ResponseEntity<Page<LoginHistoryResponse>> getAllLoginHistory(Pageable pageable) {
        return ResponseEntity.ok(activityMonitoringService.getAllLoginHistory(pageable));
    }

    @GetMapping("/failed-logins")
    public ResponseEntity<List<FailedLoginAttempt>> getFailedLogins(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(activityMonitoringService.getFailedLogins(email));
    }

    @GetMapping("/failed-logins/count")
    public ResponseEntity<Long> getFailedLoginCount(@RequestParam String email, @RequestParam(defaultValue = "30") int minutes) {
        return ResponseEntity.ok(activityMonitoringService.getFailedLoginCount(email, minutes));
    }

    @GetMapping("/permission-changes")
    public ResponseEntity<List<Object>> getPermissionChanges() {
        return ResponseEntity.ok(List.of());
    }
}