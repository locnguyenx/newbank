package com.banking.cashmanagement.dto;

import com.banking.cashmanagement.domain.enums.BatchFileFormat;
import com.banking.cashmanagement.domain.enums.BatchPaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BatchPaymentResponse {
    
    private Long id;
    private String batchReference;
    private Long customerId;
    private BatchPaymentStatus status;
    private BatchFileFormat fileFormat;
    private Integer instructionCount;
    private Integer processedCount;
    private Integer errorCount;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDate paymentDate;
    
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
}
