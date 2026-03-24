package com.banking.cashmanagement.event;

import org.springframework.context.ApplicationEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentCompletedEvent extends ApplicationEvent {
    
    private final Long paymentId;
    private final String paymentReference;
    private final Long fromAccountId;
    private final Long toAccountId;
    private final BigDecimal amount;
    private final String currency;
    private final String paymentType;
    private final LocalDateTime completedAt;
    
    public PaymentCompletedEvent(Object source, Long paymentId, String paymentReference,
            Long fromAccountId, Long toAccountId, BigDecimal amount, String currency, String paymentType) {
        super(source);
        this.paymentId = paymentId;
        this.paymentReference = paymentReference;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.paymentType = paymentType;
        this.completedAt = LocalDateTime.now();
    }
    
    public Long getPaymentId() { return paymentId; }
    public String getPaymentReference() { return paymentReference; }
    public Long getFromAccountId() { return fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPaymentType() { return paymentType; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
