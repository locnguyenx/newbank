package com.banking.limits.dto.response;

import com.banking.limits.domain.entity.ApprovalRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ApprovalRequestResponse {

    private Long id;
    private Long limitDefinitionId;
    private String transactionReference;
    private BigDecimal amount;
    private String currency;
    private String accountNumber;
    private String status;
    private String rejectionReason;
    private String approvedBy;
    private LocalDateTime decisionAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ApprovalRequestResponse() {
    }

    public static ApprovalRequestResponse fromEntity(ApprovalRequest entity) {
        ApprovalRequestResponse response = new ApprovalRequestResponse();
        response.id = entity.getId();
        response.limitDefinitionId = entity.getLimitDefinition().getId();
        response.transactionReference = entity.getTransactionReference();
        response.amount = entity.getAmount();
        response.currency = entity.getCurrency();
        response.accountNumber = entity.getAccountNumber();
        response.status = entity.getStatus().name();
        response.rejectionReason = entity.getRejectionReason();
        response.approvedBy = entity.getApprovedBy();
        response.decisionAt = entity.getDecisionAt();
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

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getDecisionAt() {
        return decisionAt;
    }

    public void setDecisionAt(LocalDateTime decisionAt) {
        this.decisionAt = decisionAt;
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
