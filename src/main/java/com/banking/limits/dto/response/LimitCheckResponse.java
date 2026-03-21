package com.banking.limits.dto.response;

import com.banking.limits.domain.enums.LimitCheckResult;

import java.math.BigDecimal;

public class LimitCheckResponse {

    private LimitCheckResult result;
    private BigDecimal effectiveLimit;
    private BigDecimal currentUsage;
    private BigDecimal remainingAmount;
    private boolean approvalRequired;
    private String rejectionReason;

    public LimitCheckResponse() {
    }

    public static LimitCheckResponse allowed(BigDecimal effectiveLimit, BigDecimal currentUsage, BigDecimal remainingAmount) {
        LimitCheckResponse response = new LimitCheckResponse();
        response.result = LimitCheckResult.ALLOWED;
        response.effectiveLimit = effectiveLimit;
        response.currentUsage = currentUsage;
        response.remainingAmount = remainingAmount;
        response.approvalRequired = false;
        return response;
    }

    public static LimitCheckResponse rejected(BigDecimal effectiveLimit, BigDecimal currentUsage, String reason) {
        LimitCheckResponse response = new LimitCheckResponse();
        response.result = LimitCheckResult.REJECTED;
        response.effectiveLimit = effectiveLimit;
        response.currentUsage = currentUsage;
        response.remainingAmount = BigDecimal.ZERO;
        response.approvalRequired = false;
        response.rejectionReason = reason;
        return response;
    }

    public static LimitCheckResponse requiresApproval(BigDecimal effectiveLimit, BigDecimal currentUsage, BigDecimal remainingAmount) {
        LimitCheckResponse response = new LimitCheckResponse();
        response.result = LimitCheckResult.REQUIRES_APPROVAL;
        response.effectiveLimit = effectiveLimit;
        response.currentUsage = currentUsage;
        response.remainingAmount = remainingAmount;
        response.approvalRequired = true;
        return response;
    }

    public static LimitCheckResponse noLimit() {
        LimitCheckResponse response = new LimitCheckResponse();
        response.result = LimitCheckResult.ALLOWED;
        response.effectiveLimit = null;
        response.currentUsage = BigDecimal.ZERO;
        response.remainingAmount = null;
        response.approvalRequired = false;
        return response;
    }

    public LimitCheckResult getResult() {
        return result;
    }

    public void setResult(LimitCheckResult result) {
        this.result = result;
    }

    public BigDecimal getEffectiveLimit() {
        return effectiveLimit;
    }

    public void setEffectiveLimit(BigDecimal effectiveLimit) {
        this.effectiveLimit = effectiveLimit;
    }

    public BigDecimal getCurrentUsage() {
        return currentUsage;
    }

    public void setCurrentUsage(BigDecimal currentUsage) {
        this.currentUsage = currentUsage;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public boolean isApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}