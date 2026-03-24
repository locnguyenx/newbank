package com.banking.cashmanagement.dto;

import com.banking.cashmanagement.domain.enums.AutoCollectionTrigger;
import com.banking.cashmanagement.domain.enums.CollectionAmountType;
import java.math.BigDecimal;

public class AutoCollectionRuleResponse {
    
    private Long id;
    private Long customerId;
    private String ruleName;
    private String description;
    private AutoCollectionTrigger triggerCondition;
    private CollectionAmountType collectionAmountType;
    private BigDecimal collectionAmountValue;
    private String currency;
    private Long fundingAccountId;
    private Boolean isActive;
    private Integer preNotificationDays;
    private Integer retryAttempts;
    private Integer retryIntervalHours;
    
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
}
