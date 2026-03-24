package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import com.banking.cashmanagement.domain.enums.BatchFileFormat;
import com.banking.cashmanagement.domain.enums.BatchPaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "batch_payment")
public class BatchPayment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "batch_reference", unique = true, nullable = false)
    private String batchReference;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BatchPaymentStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "file_format", nullable = false)
    private BatchFileFormat fileFormat;
    
    @Column(name = "instruction_count")
    private Integer instructionCount;
    
    @Column(name = "processed_count")
    private Integer processedCount;
    
    @Column(name = "error_count")
    private Integer errorCount;
    
    @Column(name = "total_amount", precision = 19, scale = 4)
    private BigDecimal totalAmount;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    
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
    public String getBatchReference() { return batchReference; }
    public void setBatchReference(String batchReference) { this.batchReference = batchReference; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public BatchPaymentStatus getStatus() { return status; }
    public void setStatus(BatchPaymentStatus status) { this.status = status; }
    public BatchFileFormat getFileFormat() { return fileFormat; }
    public void setFileFormat(BatchFileFormat fileFormat) { this.fileFormat = fileFormat; }
    public Integer getInstructionCount() { return instructionCount; }
    public void setInstructionCount(Integer instructionCount) { this.instructionCount = instructionCount; }
    public Integer getProcessedCount() { return processedCount; }
    public void setProcessedCount(Integer processedCount) { this.processedCount = processedCount; }
    public Integer getErrorCount() { return errorCount; }
    public void setErrorCount(Integer errorCount) { this.errorCount = errorCount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
