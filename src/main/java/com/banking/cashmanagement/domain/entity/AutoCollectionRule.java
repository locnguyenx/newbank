package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import com.banking.cashmanagement.domain.enums.AutoCollectionTrigger;
import com.banking.cashmanagement.domain.enums.CollectionAmountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "auto_collection_rule")
public class AutoCollectionRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "rule_name", nullable = false)
    private String ruleName;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_condition", nullable = false)
    private AutoCollectionTrigger triggerCondition;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "collection_amount_type", nullable = false)
    private CollectionAmountType collectionAmountType;
    
    @Column(name = "collection_amount_value", precision = 19, scale = 4)
    private BigDecimal collectionAmountValue;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "funding_account_id")
    private Long fundingAccountId;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "pre_notification_days")
    private Integer preNotificationDays;
    
    @Column(name = "retry_attempts")
    private Integer retryAttempts;
    
    @Column(name = "retry_interval_hours")
    private Integer retryIntervalHours;
    
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
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AutoCollectionTrigger getTriggerCondition() { return triggerCondition; }
    public void setTriggerCondition(AutoCollectionTrigger triggerCondition) { this.triggerCondition = triggerCondition; }
    public CollectionAmountType getCollectionAmountType() { return collectionAmountType; }
    public void setCollectionAmountType(CollectionAmountType collectionAmountType) { this.collectionAmountType = collectionAmountType; }
    public BigDecimal getCollectionAmountValue() { return collectionAmountValue; }
    public void setCollectionAmountValue(BigDecimal collectionAmountValue) { this.collectionAmountValue = collectionAmountValue; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Long getFundingAccountId() { return fundingAccountId; }
    public void setFundingAccountId(Long fundingAccountId) { this.fundingAccountId = fundingAccountId; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getPreNotificationDays() { return preNotificationDays; }
    public void setPreNotificationDays(Integer preNotificationDays) { this.preNotificationDays = preNotificationDays; }
    public Integer getRetryAttempts() { return retryAttempts; }
    public void setRetryAttempts(Integer retryAttempts) { this.retryAttempts = retryAttempts; }
    public Integer getRetryIntervalHours() { return retryIntervalHours; }
    public void setRetryIntervalHours(Integer retryIntervalHours) { this.retryIntervalHours = retryIntervalHours; }
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
