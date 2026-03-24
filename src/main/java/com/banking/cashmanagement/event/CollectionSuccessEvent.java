package com.banking.cashmanagement.event;

import org.springframework.context.ApplicationEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CollectionSuccessEvent extends ApplicationEvent {
    
    private final Long autoCollectionAttemptId;
    private final Long ruleId;
    private final Long customerId;
    private final Long accountId;
    private final BigDecimal collectedAmount;
    private final String currency;
    private final LocalDateTime collectedAt;
    
    public CollectionSuccessEvent(Object source, Long autoCollectionAttemptId, Long ruleId,
            Long customerId, Long accountId, BigDecimal collectedAmount, String currency) {
        super(source);
        this.autoCollectionAttemptId = autoCollectionAttemptId;
        this.ruleId = ruleId;
        this.customerId = customerId;
        this.accountId = accountId;
        this.collectedAmount = collectedAmount;
        this.currency = currency;
        this.collectedAt = LocalDateTime.now();
    }
    
    public Long getAutoCollectionAttemptId() { return autoCollectionAttemptId; }
    public Long getRuleId() { return ruleId; }
    public Long getCustomerId() { return customerId; }
    public Long getAccountId() { return accountId; }
    public BigDecimal getCollectedAmount() { return collectedAmount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getCollectedAt() { return collectedAt; }
}
