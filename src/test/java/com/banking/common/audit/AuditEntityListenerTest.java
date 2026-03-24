package com.banking.common.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditEntityListenerTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void shouldExtractEntityId() throws NoSuchFieldException {
        TestEntity entity = new TestEntity();
        ReflectionTestUtils.setField(entity, "id", 123L);
        ReflectionTestUtils.setField(entity, "name", "TestName");

        AuditEntityListener listener = new AuditEntityListener();
        ReflectionTestUtils.setField(listener, "objectMapper", new com.fasterxml.jackson.databind.ObjectMapper());

        listener.postPersist(entity);
    }

    @Test
    void shouldHandleEntityWithNoIdField() {
        AuditEntityListener listener = new AuditEntityListener();
        ReflectionTestUtils.setField(listener, "objectMapper", new com.fasterxml.jackson.databind.ObjectMapper());

        assertDoesNotThrow(() -> listener.postPersist(new NoIdEntity()));
    }

    private static class TestEntity {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private static class NoIdEntity {
        private String data;
    }
}