package com.banking.limits.api.dto;

import java.math.BigDecimal;

public class LimitCheckResult {

    private boolean approved;
    private String reason;
    private BigDecimal remainingLimit;

    public LimitCheckResult() {
    }

    public LimitCheckResult(boolean approved, String reason, BigDecimal remainingLimit) {
        this.approved = approved;
        this.reason = reason;
        this.remainingLimit = remainingLimit;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getRemainingLimit() {
        return remainingLimit;
    }

    public void setRemainingLimit(BigDecimal remainingLimit) {
        this.remainingLimit = remainingLimit;
    }
}
