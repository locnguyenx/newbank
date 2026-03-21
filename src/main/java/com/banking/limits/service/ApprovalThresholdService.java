package com.banking.limits.service;

import com.banking.limits.domain.entity.ApprovalThreshold;
import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.embeddable.AuditFields;
import com.banking.limits.dto.response.ApprovalThresholdResponse;
import com.banking.limits.exception.LimitNotFoundException;
import com.banking.limits.repository.ApprovalThresholdRepository;
import com.banking.limits.repository.LimitDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@Transactional
public class ApprovalThresholdService {

    private final ApprovalThresholdRepository approvalThresholdRepository;
    private final LimitDefinitionRepository limitDefinitionRepository;

    public ApprovalThresholdService(ApprovalThresholdRepository approvalThresholdRepository,
                                     LimitDefinitionRepository limitDefinitionRepository) {
        this.approvalThresholdRepository = approvalThresholdRepository;
        this.limitDefinitionRepository = limitDefinitionRepository;
    }

    public ApprovalThresholdResponse createOrUpdateThreshold(Long limitId, BigDecimal absoluteAmount, Integer percentageOfLimit) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));

        ApprovalThreshold threshold = approvalThresholdRepository.findByLimitDefinitionId(limitId)
                .orElseGet(() -> new ApprovalThreshold(limitDefinition, null, null));

        threshold.setLimitDefinition(limitDefinition);
        threshold.setAbsoluteAmount(absoluteAmount);
        threshold.setPercentageOfLimit(percentageOfLimit);

        if (threshold.getAudit() == null) {
            threshold.setAudit(new AuditFields());
        }

        ApprovalThreshold saved = approvalThresholdRepository.save(threshold);
        return ApprovalThresholdResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ApprovalThresholdResponse getThreshold(Long limitId) {
        ApprovalThreshold threshold = approvalThresholdRepository.findByLimitDefinitionId(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));
        return ApprovalThresholdResponse.fromEntity(threshold);
    }

    public BigDecimal calculateThresholdAmount(Long limitId, BigDecimal effectiveLimit) {
        ApprovalThreshold threshold = approvalThresholdRepository.findByLimitDefinitionId(limitId)
                .orElse(null);

        if (threshold == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal calculatedAmount = BigDecimal.ZERO;

        if (threshold.getAbsoluteAmount() != null) {
            calculatedAmount = calculatedAmount.max(threshold.getAbsoluteAmount());
        }

        if (threshold.getPercentageOfLimit() != null && effectiveLimit != null) {
            BigDecimal percentageAmount = effectiveLimit
                    .multiply(BigDecimal.valueOf(threshold.getPercentageOfLimit()))
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            calculatedAmount = calculatedAmount.max(percentageAmount);
        }

        return calculatedAmount;
    }

    public void setThreshold(Long limitId, BigDecimal absoluteAmount, Integer percentage) {
        LimitDefinition limitDefinition = limitDefinitionRepository.findById(limitId)
                .orElseThrow(() -> new LimitNotFoundException(limitId));

        Optional<ApprovalThreshold> existing = approvalThresholdRepository.findByLimitDefinitionId(limitId);

        ApprovalThreshold threshold;
        if (existing.isPresent()) {
            threshold = existing.get();
            threshold.setAbsoluteAmount(absoluteAmount);
            threshold.setPercentageOfLimit(percentage);
        } else {
            threshold = new ApprovalThreshold(limitDefinition, absoluteAmount, percentage);
        }

        approvalThresholdRepository.save(threshold);
    }

    @Transactional(readOnly = true)
    public boolean exceedsThreshold(Long limitId, BigDecimal amount) {
        Optional<ApprovalThreshold> thresholdOpt = approvalThresholdRepository.findByLimitDefinitionId(limitId);

        if (thresholdOpt.isEmpty()) {
            return false;
        }

        ApprovalThreshold threshold = thresholdOpt.get();

        if (threshold.getAbsoluteAmount() != null) {
            return amount.compareTo(threshold.getAbsoluteAmount()) > 0;
        }

        if (threshold.getPercentageOfLimit() != null) {
            LimitDefinition limitDefinition = threshold.getLimitDefinition();
            BigDecimal thresholdAmount = limitDefinition.getAmount()
                    .multiply(BigDecimal.valueOf(threshold.getPercentageOfLimit()))
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            return amount.compareTo(thresholdAmount) > 0;
        }

        return false;
    }
}
