package com.banking.cashmanagement.event;

import org.springframework.context.ApplicationEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PayrollProcessedEvent extends ApplicationEvent {
    
    private final Long payrollBatchId;
    private final String batchReference;
    private final Long customerId;
    private final BigDecimal totalAmount;
    private final String currency;
    private final LocalDateTime processedAt;
    
    public PayrollProcessedEvent(Object source, Long payrollBatchId, String batchReference,
            Long customerId, BigDecimal totalAmount, String currency) {
        super(source);
        this.payrollBatchId = payrollBatchId;
        this.batchReference = batchReference;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.processedAt = LocalDateTime.now();
    }
    
    public Long getPayrollBatchId() { return payrollBatchId; }
    public String getBatchReference() { return batchReference; }
    public Long getCustomerId() { return customerId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getProcessedAt() { return processedAt; }
}
