package com.banking.common.security.iam.service;

import com.banking.common.security.rbac.AmountThreshold;
import com.banking.common.security.rbac.AmountThresholdRepository;
import com.banking.common.security.rbac.Role;
import com.banking.common.security.iam.dto.ThresholdRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThresholdManagementServiceTest {

    @Mock
    private AmountThresholdRepository thresholdRepository;

    @InjectMocks
    private ThresholdManagementService thresholdManagementService;

    private AmountThreshold existingThreshold;
    private ThresholdRequest thresholdRequest;

    @BeforeEach
    void setUp() {
        existingThreshold = new AmountThreshold(1L, Role.DEPARTMENT_CHECKER, new BigDecimal("10000.0000"));
        existingThreshold.setId(1L);

        thresholdRequest = new ThresholdRequest();
        thresholdRequest.setUserId(1L);
        thresholdRequest.setRole(Role.DEPARTMENT_CHECKER);
        thresholdRequest.setThreshold(new BigDecimal("15000.0000"));
    }

    @Test
    void setThreshold_CreatesNewWhenNotExists() {
        when(thresholdRepository.findAll()).thenReturn(Collections.emptyList());
        when(thresholdRepository.save(any(AmountThreshold.class))).thenAnswer(invocation -> {
            AmountThreshold t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        AmountThreshold result = thresholdManagementService.setThreshold(thresholdRequest);

        assertNotNull(result);
        assertEquals(new BigDecimal("15000.0000"), result.getThreshold());
    }

    @Test
    void setThreshold_UpdatesExisting() {
        when(thresholdRepository.findAll()).thenReturn(Arrays.asList(existingThreshold));
        when(thresholdRepository.save(any(AmountThreshold.class))).thenReturn(existingThreshold);

        AmountThreshold result = thresholdManagementService.setThreshold(thresholdRequest);

        assertNotNull(result);
        verify(thresholdRepository).save(existingThreshold);
    }

    @Test
    void getThreshold_Success() {
        when(thresholdRepository.findById(1L)).thenReturn(Optional.of(existingThreshold));

        AmountThreshold result = thresholdManagementService.getThreshold(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserThresholds_Success() {
        when(thresholdRepository.findAll()).thenReturn(Arrays.asList(existingThreshold));

        List<AmountThreshold> results = thresholdManagementService.getUserThresholds(1L);

        assertEquals(1, results.size());
    }

    @Test
    void deleteThreshold_Success() {
        doNothing().when(thresholdRepository).deleteById(1L);

        thresholdManagementService.deleteThreshold(1L);

        verify(thresholdRepository).deleteById(1L);
    }
}