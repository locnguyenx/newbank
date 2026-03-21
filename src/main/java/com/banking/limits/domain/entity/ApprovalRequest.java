package com.banking.limits.domain.entity;

import com.banking.limits.domain.embeddable.AuditFields;
import com.banking.limits.domain.enums.ApprovalStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "transaction_reference", nullable = false, length = 50)
    private String transactionReference;

    @Column(precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency;

    @Column(length = 30)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "decision_at")
    private LocalDateTime decisionAt;

    @Embedded
    private AuditFields audit;

    protected ApprovalRequest() {
    }

    public ApprovalRequest(LimitDefinition limitDefinition, String transactionReference,
                           BigDecimal amount, String currency, String accountNumber) {
        this.limitDefinition = limitDefinition;
        this.transactionReference = transactionReference;
        this.amount = amount;
        this.currency = currency;
        this.accountNumber = accountNumber;
        this.status = ApprovalStatus.PENDING;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public LimitDefinition getLimitDefinition() {
        return limitDefinition;
    }

    public void setLimitDefinition(LimitDefinition limitDefinition) {
        this.limitDefinition = limitDefinition;
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

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
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

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
