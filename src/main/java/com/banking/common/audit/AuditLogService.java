package com.banking.common.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    @Transactional
    public void log(AuditAction action, String entityType, Long entityId,
                    String beforeJson, String afterJson, AuditContext context) {
        try {
            AuditLog entry = new AuditLog(action, entityType, entityId, beforeJson, afterJson, context);
            auditLogRepository.save(entry);
            log.debug("Audit log created: {} {} {}", action, entityType, entityId);
        } catch (Exception e) {
            log.error("Failed to create audit log: {} {} {}", action, entityType, entityId, e);
        }
    }
}