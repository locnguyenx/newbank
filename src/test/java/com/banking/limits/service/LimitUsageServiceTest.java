package com.banking.limits.service;

import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.entity.LimitUsage;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.repository.LimitDefinitionRepository;
import com.banking.limits.repository.LimitUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LimitUsageServiceTest {

    @Mock
    private LimitUsageRepository limitUsageRepository;

    @Mock
    private LimitDefinitionRepository limitDefinitionRepository;

    private LimitUsageService limitUsageService;

    @BeforeEach
    void setUp() {
        limitUsageService = new LimitUsageService(limitUsageRepository, limitDefinitionRepository);
    }

    private LimitDefinition dailyDefinition() {
        return new LimitDefinition("Daily Limit", LimitType.DAILY, new BigDecimal("50000.00"), "USD");
    }

    private LimitDefinition weeklyDefinition() {
        return new LimitDefinition("Weekly Limit", LimitType.WEEKLY, new BigDecimal("200000.00"), "USD");
    }

    private LimitDefinition monthlyDefinition() {
        return new LimitDefinition("Monthly Limit", LimitType.MONTHLY, new BigDecimal("500000.00"), "USD");
    }

    @Test
    void recordUsage_incrementsCumulative() {
        LimitDefinition def = dailyDefinition();
        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(def));
        when(limitUsageRepository.findByLimitDefinitionIdAndAccountNumberAndPeriodStart(eq(1L), eq("ACC-001"), any()))
                .thenReturn(Optional.empty());
        when(limitUsageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        limitUsageService.recordUsage(1L, "ACC-001", new BigDecimal("1000.00"), LocalDate.of(2026, 3, 21));

        ArgumentCaptor<LimitUsage> captor = ArgumentCaptor.forClass(LimitUsage.class);
        verify(limitUsageRepository).save(captor.capture());
        LimitUsage saved = captor.getValue();
        assertEquals(0, new BigDecimal("1000.00").compareTo(saved.getCumulativeAmount()));
        assertEquals("ACC-001", saved.getAccountNumber());
        assertEquals(LocalDate.of(2026, 3, 21), saved.getPeriodStart());
    }

    @Test
    void recordUsage_incrementsCumulative_existingRecord() {
        LimitDefinition def = dailyDefinition();
        LimitUsage existing = new LimitUsage(def, "ACC-001", LocalDate.of(2026, 3, 21), LocalDate.of(2026, 3, 21));
        existing.addAmount(new BigDecimal("1000.00"));

        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(def));
        when(limitUsageRepository.findByLimitDefinitionIdAndAccountNumberAndPeriodStart(eq(1L), eq("ACC-001"), any()))
                .thenReturn(Optional.of(existing));
        when(limitUsageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        limitUsageService.recordUsage(1L, "ACC-001", new BigDecimal("500.00"), LocalDate.of(2026, 3, 21));

        ArgumentCaptor<LimitUsage> captor = ArgumentCaptor.forClass(LimitUsage.class);
        verify(limitUsageRepository).save(captor.capture());
        assertEquals(0, new BigDecimal("1500.00").compareTo(captor.getValue().getCumulativeAmount()));
    }

    @Test
    void getCumulativeUsage_returnsCorrectTotal() {
        LimitDefinition def = dailyDefinition();
        LimitUsage usage = new LimitUsage(def, "ACC-001", LocalDate.of(2026, 3, 21), LocalDate.of(2026, 3, 21));
        usage.addAmount(new BigDecimal("3000.00"));

        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(def));
        when(limitUsageRepository.findByLimitDefinitionIdAndAccountNumberAndPeriodStart(1L, "ACC-001", LocalDate.of(2026, 3, 21)))
                .thenReturn(Optional.of(usage));

        BigDecimal result = limitUsageService.getCumulativeUsage(1L, "ACC-001", LocalDate.of(2026, 3, 21));

        assertEquals(0, new BigDecimal("3000.00").compareTo(result));
    }

    @Test
    void getCumulativeUsage_returnsZeroWhenNoRecord() {
        LimitDefinition def = dailyDefinition();
        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(def));
        when(limitUsageRepository.findByLimitDefinitionIdAndAccountNumberAndPeriodStart(1L, "ACC-001", LocalDate.of(2026, 3, 21)))
                .thenReturn(Optional.empty());

        BigDecimal result = limitUsageService.getCumulativeUsage(1L, "ACC-001", LocalDate.of(2026, 3, 21));

        assertEquals(0, BigDecimal.ZERO.compareTo(result));
    }

    @Test
    void periodStart_daily_returnsSameDay() {
        LimitDefinition def = dailyDefinition();
        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(def));
        when(limitUsageRepository.findByLimitDefinitionIdAndAccountNumberAndPeriodStart(eq(1L), eq("ACC-001"), any()))
                .thenReturn(Optional.empty());
        when(limitUsageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LocalDate date = LocalDate.of(2026, 3, 21);
        limitUsageService.recordUsage(1L, "ACC-001", new BigDecimal("100.00"), date);

        ArgumentCaptor<LimitUsage> captor = ArgumentCaptor.forClass(LimitUsage.class);
        verify(limitUsageRepository).save(captor.capture());
        assertEquals(LocalDate.of(2026, 3, 21), captor.getValue().getPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 21), captor.getValue().getPeriodEnd());
    }

    @Test
    void periodStart_weekly_returnsMonday() {
        LimitDefinition def = weeklyDefinition();
        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(def));
        when(limitUsageRepository.findByLimitDefinitionIdAndAccountNumberAndPeriodStart(eq(1L), eq("ACC-001"), any()))
                .thenReturn(Optional.empty());
        when(limitUsageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Saturday 2026-03-21
        LocalDate date = LocalDate.of(2026, 3, 21);
        limitUsageService.recordUsage(1L, "ACC-001", new BigDecimal("100.00"), date);

        ArgumentCaptor<LimitUsage> captor = ArgumentCaptor.forClass(LimitUsage.class);
        verify(limitUsageRepository).save(captor.capture());
        assertEquals(LocalDate.of(2026, 3, 16), captor.getValue().getPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 22), captor.getValue().getPeriodEnd());
    }

    @Test
    void periodStart_monthly_returnsFirstOfMonth() {
        LimitDefinition def = monthlyDefinition();
        when(limitDefinitionRepository.findById(1L)).thenReturn(Optional.of(def));
        when(limitUsageRepository.findByLimitDefinitionIdAndAccountNumberAndPeriodStart(eq(1L), eq("ACC-001"), any()))
                .thenReturn(Optional.empty());
        when(limitUsageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LocalDate date = LocalDate.of(2026, 3, 21);
        limitUsageService.recordUsage(1L, "ACC-001", new BigDecimal("100.00"), date);

        ArgumentCaptor<LimitUsage> captor = ArgumentCaptor.forClass(LimitUsage.class);
        verify(limitUsageRepository).save(captor.capture());
        assertEquals(LocalDate.of(2026, 3, 1), captor.getValue().getPeriodStart());
        assertEquals(LocalDate.of(2026, 3, 31), captor.getValue().getPeriodEnd());
    }
}
