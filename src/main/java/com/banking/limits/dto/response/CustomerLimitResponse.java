package com.banking.limits.dto.response;

import com.banking.limits.domain.entity.CustomerLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerLimitResponse {

    private Long id;
    private Long limitDefinitionId;
    private String limitName;
    private Long customerId;
    private BigDecimal overrideAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomerLimitResponse() {
    }

    public static CustomerLimitResponse fromEntity(CustomerLimit entity) {
        CustomerLimitResponse response = new CustomerLimitResponse();
        response.id = entity.getId();
        response.limitDefinitionId = entity.getLimitDefinition().getId();
        response.limitName = entity.getLimitDefinition().getName();
        response.customerId = entity.getCustomerId();
        response.overrideAmount = entity.getOverrideAmount();
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOverrideAmount() {
        return overrideAmount;
    }

    public void setOverrideAmount(BigDecimal overrideAmount) {
        this.overrideAmount = overrideAmount;
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
