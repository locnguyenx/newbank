package com.banking.cashmanagement.dto;

import com.banking.cashmanagement.domain.enums.PayrollBatchStatus;
import com.banking.cashmanagement.domain.enums.PayrollFileFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PayrollBatchResponse {
    
    private Long id;
    private String batchReference;
    private Long customerId;
    private PayrollBatchStatus status;
    private PayrollFileFormat fileFormat;
    private Integer recordCount;
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
    public PayrollBatchStatus getStatus() { return status; }
    public void setStatus(PayrollBatchStatus status) { this.status = status; }
    public PayrollFileFormat getFileFormat() { return fileFormat; }
    public void setFileFormat(PayrollFileFormat fileFormat) { this.fileFormat = fileFormat; }
    public Integer getRecordCount() { return recordCount; }
    public void setRecordCount(Integer recordCount) { this.recordCount = recordCount; }
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
