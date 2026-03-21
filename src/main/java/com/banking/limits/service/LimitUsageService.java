package com.banking.limits.service;

import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.entity.LimitUsage;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.exception.LimitNotFoundException;
import com.banking.limits.repository.LimitDefinitionRepository;
import com.banking.limits.repository.LimitUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@Transactional
public class LimitUsageService {

    private final LimitUsageRepository limitUsageRepository;
    private final LimitDefinitionRepository limitDefinitionRepository;

    public LimitUsageService(LimitUsageRepository limitUsageRepository,
                             LimitDefinitionRepository limitDefinitionRepository) {
        this.limitUsageRepository = limitUsageRepository;
        this.limitDefinitionRepository = limitDefinitionRepository;
    }

    /**
     * Records a transaction amount against a limit for cumulative usage tracking.
     * NOTE: This method must be wired from the transaction processing service
     * (e.g., a TransactionService or AccountPostingService) to track actual usage.
     * Currently not called from production code — limits will always show zero usage.
     */
    public void recordUsage(Long limitId, String accountNumber, BigDecimal amount, LocalDate date) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));

        LocalDate periodStart = calculatePeriodStart(date, limitDefinition.getLimitType());
        LocalDate periodEnd = calculatePeriodEnd(date, limitDefinition.getLimitType());

        LimitUsage usage = limitUsageRepository
                .findByLimitDefinitionIdAndAccountNumberAndPeriodStart(limitId, accountNumber, periodStart)
                .orElse(new LimitUsage(limitDefinition, accountNumber, periodStart, periodEnd));

        usage.addAmount(amount);
        limitUsageRepository.save(usage);
    }

    @Transactional(readOnly = true)
    public BigDecimal getCumulativeUsage(Long limitId, String accountNumber, LocalDate date) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));

        LocalDate periodStart = calculatePeriodStart(date, limitDefinition.getLimitType());

        return limitUsageRepository
                .findByLimitDefinitionIdAndAccountNumberAndPeriodStart(limitId, accountNumber, periodStart)
                .map(LimitUsage::getCumulativeAmount)
                .orElse(BigDecimal.ZERO);
    }

    private LocalDate calculatePeriodStart(LocalDate date, LimitType type) {
        return switch (type) {
            case DAILY -> date;
            case WEEKLY -> date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case MONTHLY -> date.withDayOfMonth(1);
            case PER_TRANSACTION -> date;
        };
    }

    private LocalDate calculatePeriodEnd(LocalDate date, LimitType type) {
        return switch (type) {
            case DAILY -> date;
            case WEEKLY -> date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            case MONTHLY -> date.with(TemporalAdjusters.lastDayOfMonth());
            case PER_TRANSACTION -> date;
        };
    }
}
