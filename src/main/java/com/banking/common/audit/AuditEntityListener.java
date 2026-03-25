package com.banking.common.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AuditEntityListener {

    private static final Logger log = LoggerFactory.getLogger(AuditEntityListener.class);
    private static ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    
    private static AuditLogService auditLogService;

    @Autowired(required = false)
    public void setAuditLogService(AuditLogService service) {
        AuditEntityListener.auditLogService = service;
    }

    @PostPersist
    public void postPersist(Object entity) {
        logAudit(entity, AuditAction.CREATE, null);
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        logAudit(entity, AuditAction.UPDATE, null);
    }

    @PreRemove
    public void preRemove(Object entity) {
        logAudit(entity, AuditAction.DELETE, null);
    }

    private void logAudit(Object entity, AuditAction action, String beforeJson) {
        try {
            String entityType = entity.getClass().getSimpleName();
            Long entityId = getEntityId(entity);
            String afterJson = objectMapper.writeValueAsString(entity);

            if (auditLogService != null) {
                CompletableFuture.runAsync(() -> {
                    try {
                        auditLogService.log(action, entityType, entityId, beforeJson, afterJson);
                    } catch (Exception e) {
                        log.error("Failed to persist audit log", e);
                    }
                });
            }

            log.debug("Audit: {} {} id={}", action, entityType, entityId);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize entity for audit", e);
        }
    }

    private Long getEntityId(Object entity) {
        try {
            var idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return (Long) idField.get(entity);
        } catch (Exception e) {
            log.warn("Could not extract entity ID for audit", e);
            return null;
        }
    }
}