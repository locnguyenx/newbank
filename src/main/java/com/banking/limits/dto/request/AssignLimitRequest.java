package com.banking.limits.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AssignLimitRequest {

    @NotNull(message = "Limit ID is required")
    private Long limitId;

    @NotNull(message = "Reference ID is required")
    private String referenceId;

    private BigDecimal overrideAmount;

    public AssignLimitRequest() {
    }

    public Long getLimitId() {
        return limitId;
    }

    public void setLimitId(Long limitId) {
        this.limitId = limitId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public BigDecimal getOverrideAmount() {
        return overrideAmount;
    }

    public void setOverrideAmount(BigDecimal overrideAmount) {
        this.overrideAmount = overrideAmount;
    }
}
