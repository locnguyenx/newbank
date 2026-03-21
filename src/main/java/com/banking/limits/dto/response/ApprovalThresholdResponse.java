package com.banking.limits.dto.response;

import com.banking.limits.domain.entity.ApprovalThreshold;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ApprovalThresholdResponse {

    private Long id;
    private Long limitDefinitionId;
    private String limitName;
    private BigDecimal absoluteAmount;
    private Integer percentageOfLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ApprovalThresholdResponse() {
    }

    public static ApprovalThresholdResponse fromEntity(ApprovalThreshold entity) {
        ApprovalThresholdResponse response = new ApprovalThresholdResponse();
        response.id = entity.getId();
        response.limitDefinitionId = entity.getLimitDefinition().getId();
        response.limitName = entity.getLimitDefinition().getName();
        response.absoluteAmount = entity.getAbsoluteAmount();
        response.percentageOfLimit = entity.getPercentageOfLimit();
        if (entity.getAudit() != null) {
            response.createdAt = entity.getAudit().getCreatedAt();
            response.updatedAt = entity.getAudit().getUpdatedAt();
        }
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLimitDefinitionId() {
        return limitDefinitionId;
    }

    public void setLimitDefinitionId(Long limitDefinitionId) {
        this.limitDefinitionId = limitDefinitionId;
    }

    public String getLimitName() {
        return limitName;
    }

    public void setLimitName(String limitName) {
        this.limitName = limitName;
    }

    public BigDecimal getAbsoluteAmount() {
        return absoluteAmount;
    }

    public void setAbsoluteAmount(BigDecimal absoluteAmount) {
        this.absoluteAmount = absoluteAmount;
    }

    public Integer getPercentageOfLimit() {
        return percentageOfLimit;
    }

    public void setPercentageOfLimit(Integer percentageOfLimit) {
        this.percentageOfLimit = percentageOfLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
