package com.banking.common.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        auditLogService = new AuditLogService(auditLogRepository);
    }

    @Test
    void shouldSaveAuditLogEntry() {
        AuditAction action = AuditAction.CREATE;
        String entityType = "User";
        Long entityId = 1L;
        String beforeJson = null;
        String afterJson = "{\"email\":\"test@example.com\"}";
        AuditContext context = mock(AuditContext.class);
        when(context.getUserId()).thenReturn(100L);
        when(context.getActorType()).thenReturn(ActorType.USER);
        when(context.getIpAddress()).thenReturn("192.168.1.1");
        when(context.getCorrelationId()).thenReturn("corr-123");

        auditLogService.log(action, entityType, entityId, beforeJson, afterJson, context);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());

        AuditLog savedLog = captor.getValue();
        assertEquals(action, savedLog.getAction());
        assertEquals(entityType, savedLog.getEntityType());
        assertEquals(entityId, savedLog.getEntityId());
        assertEquals(beforeJson, savedLog.getBeforeJson());
        assertEquals(afterJson, savedLog.getAfterJson());
        assertEquals(100L, savedLog.getActorUserId());
        assertEquals(ActorType.USER, savedLog.getActorType());
        assertEquals("192.168.1.1", savedLog.getIpAddress());
        assertEquals("corr-123", savedLog.getCorrelationId());
        assertNotNull(savedLog.getTimestamp());
    }

    @Test
    void shouldHandleNullContext() {
        AuditAction action = AuditAction.UPDATE;
        String entityType = "Account";
        Long entityId = 42L;
        String beforeJson = "{\"name\":\"old\"}";
        String afterJson = "{\"name\":\"new\"}";

        auditLogService.log(action, entityType, entityId, beforeJson, afterJson, null);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());

        AuditLog savedLog = captor.getValue();
        assertEquals(action, savedLog.getAction());
        assertEquals(entityType, savedLog.getEntityType());
        assertEquals(entityId, savedLog.getEntityId());
        assertNull(savedLog.getActorUserId());
        assertNull(savedLog.getActorType());
    }
}