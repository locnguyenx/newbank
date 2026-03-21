package com.banking.limits.service;

import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.enums.LimitStatus;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.dto.request.CreateLimitDefinitionRequest;
import com.banking.limits.dto.response.LimitDefinitionResponse;
import com.banking.limits.exception.InvalidLimitTypeException;
import com.banking.limits.exception.LimitNotFoundException;
import com.banking.limits.mapper.LimitMapper;
import com.banking.limits.repository.LimitDefinitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimitDefinitionServiceTest {

    @Mock
    private LimitDefinitionRepository limitDefinitionRepository;

    @Mock
    private LimitMapper limitMapper;

    private LimitDefinitionService limitDefinitionService;

    @BeforeEach
    void setUp() {
        limitDefinitionService = new LimitDefinitionService(limitDefinitionRepository, limitMapper);
    }

    private CreateLimitDefinitionRequest createRequest(String name, String limitType, BigDecimal amount, String currency) {
        CreateLimitDefinitionRequest request = new CreateLimitDefinitionRequest();
        request.setName(name);
        request.setLimitType(limitType);
        request.setAmount(amount);
        request.setCurrency(currency);
        return request;
    }

    @Test
    void createLimit_success() {
        CreateLimitDefinitionRequest request = createRequest("Daily Transfer", "DAILY", new BigDecimal("50000.00"), "USD");
        LimitDefinition entity = new LimitDefinition("Daily Transfer", LimitType.DAILY, new BigDecimal("50000.00"), "USD");

        when(limitMapper.toEntity(request)).thenReturn(entity);
        when(limitDefinitionRepository.save(any())).thenReturn(entity);
        when(limitMapper.toResponse(entity)).thenReturn(LimitDefinitionResponse.fromEntity(entity));

        LimitDefinitionResponse response = limitDefinitionService.createLimit(request);

        assertEquals("Daily Transfer", response.getName());
        assertEquals("DAILY", response.getLimitType());
        assertEquals(0, new BigDecimal("50000.00").compareTo(response.getAmount()));
        assertEquals("USD", response.getCurrency());
        assertEquals("ACTIVE", response.getStatus());

        verify(limitDefinitionRepository).save(any());
    }

    @Test
    void createLimit_invalidType_throws() {
        CreateLimitDefinitionRequest request = createRequest("Bad Limit", "INVALID", new BigDecimal("1000.00"), "USD");

        when(limitMapper.toEntity(request)).thenThrow(new InvalidLimitTypeException("INVALID"));

        assertThrows(InvalidLimitTypeException.class, () ->
                limitDefinitionService.createLimit(request));

        verify(limitDefinitionRepository, never()).save(any());
    }

    @Test
    void getLimit_notFound_throws() {
        when(limitDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(LimitNotFoundException.class, () ->
                limitDefinitionService.getLimit(999L));
    }

    @Test
    void deactivateLimit_success() {
        LimitDefinition activeEntity = new LimitDefinition("Daily Transfer", LimitType.DAILY, new BigDecimal("50000.00"), "USD");
        LimitDefinition deactivatedEntity = new LimitDefinition("Daily Transfer", LimitType.DAILY, new BigDecimal("50000.00"), "USD");
        deactivatedEntity.setStatus(LimitStatus.INACTIVE);

        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(activeEntity));
        when(limitDefinitionRepository.save(any())).thenReturn(deactivatedEntity);
        when(limitMapper.toResponse(deactivatedEntity)).thenReturn(LimitDefinitionResponse.fromEntity(deactivatedEntity));

        LimitDefinitionResponse response = limitDefinitionService.deactivateLimit(1L);

        assertEquals("INACTIVE", response.getStatus());
        verify(limitDefinitionRepository).save(any());
    }

    @Test
    void activateLimit_success() {
        LimitDefinition inactiveEntity = new LimitDefinition("Daily Transfer", LimitType.DAILY, new BigDecimal("50000.00"), "USD");
        inactiveEntity.setStatus(LimitStatus.INACTIVE);
        LimitDefinition activatedEntity = new LimitDefinition("Daily Transfer", LimitType.DAILY, new BigDecimal("50000.00"), "USD");
        activatedEntity.setStatus(LimitStatus.ACTIVE);

        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(inactiveEntity));
        when(limitDefinitionRepository.save(any())).thenReturn(activatedEntity);
        when(limitMapper.toResponse(activatedEntity)).thenReturn(LimitDefinitionResponse.fromEntity(activatedEntity));

        LimitDefinitionResponse response = limitDefinitionService.activateLimit(1L);

        assertEquals("ACTIVE", response.getStatus());
        verify(limitDefinitionRepository).save(any());
    }
}
