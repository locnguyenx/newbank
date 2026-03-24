package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import com.banking.cashmanagement.domain.enums.AutoCollectionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "auto_collection_attempt")
public class AutoCollectionAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "auto_collection_rule_id", nullable = false)
    private Long autoCollectionRuleId;
    
    @Column(name = "invoice_id")
    private Long invoiceId;
    
    @Column(name = "attempt_date_time", nullable = false)
    private LocalDateTime attemptDateTime;
    
    @Column(name = "collection_amount", precision = 19, scale = 4)
    private BigDecimal collectionAmount;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AutoCollectionStatus status;
    
    @Column(name = "error_code")
    private String errorCode;
    
    @Column(name = "error_description")
    private String errorDescription;
    
    @Embedded
    private AuditFields auditFields = new AuditFields();
    
    @PrePersist
    protected void onCreate() {
        auditFields.setCreatedAt(java.time.LocalDateTime.now());
        auditFields.setCreatedBy("system");
    }
    
    @PreUpdate
    protected void onUpdate() {
        auditFields.setUpdatedAt(java.time.LocalDateTime.now());
        auditFields.setUpdatedBy("system");
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAutoCollectionRuleId() { return autoCollectionRuleId; }
    public void setAutoCollectionRuleId(Long autoCollectionRuleId) { this.autoCollectionRuleId = autoCollectionRuleId; }
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public LocalDateTime getAttemptDateTime() { return attemptDateTime; }
    public void setAttemptDateTime(LocalDateTime attemptDateTime) { this.attemptDateTime = attemptDateTime; }
    public BigDecimal getCollectionAmount() { return collectionAmount; }
    public void setCollectionAmount(BigDecimal collectionAmount) { this.collectionAmount = collectionAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public AutoCollectionStatus getStatus() { return status; }
    public void setStatus(AutoCollectionStatus status) { this.status = status; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorDescription() { return errorDescription; }
    public void setErrorDescription(String errorDescription) { this.errorDescription = errorDescription; }
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
