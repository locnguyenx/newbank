package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import com.banking.cashmanagement.domain.enums.CashPoolingStatus;
import com.banking.cashmanagement.domain.enums.CashPoolingType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_pooling_transaction")
public class CashPoolingTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "pool_reference", unique = true, nullable = false)
    private String poolReference;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    
    @Column(name = "from_account_id")
    private Long fromAccountId;
    
    @Column(name = "to_account_id")
    private Long toAccountId;
    
    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pooling_type", nullable = false)
    private CashPoolingType poolingType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CashPoolingStatus status;
    
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
    public String getPoolReference() { return poolReference; }
    public void setPoolReference(String poolReference) { this.poolReference = poolReference; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public CashPoolingType getPoolingType() { return poolingType; }
    public void setPoolingType(CashPoolingType poolingType) { this.poolingType = poolingType; }
    public CashPoolingStatus getStatus() { return status; }
    public void setStatus(CashPoolingStatus status) { this.status = status; }
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
