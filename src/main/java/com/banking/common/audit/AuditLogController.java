package com.banking.common.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'HO_ADMIN')")
    public ResponseEntity<Page<AuditLog>> getLogs(
            @PageableDefault(size = 20, sort = "timestamp") Pageable pageable) {
        Page<AuditLog> logs = auditLogRepository.findAll(pageable);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs/{id}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'HO_ADMIN')")
    public ResponseEntity<AuditLog> getLog(@PathVariable Long id) {
        return auditLogRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}