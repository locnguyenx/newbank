package com.banking.cashmanagement.dto;

import com.banking.cashmanagement.domain.enums.PayrollFileFormat;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PayrollBatchRequest {
    
    @NotNull
    private Long customerId;
    
    @NotNull
    private PayrollFileFormat fileFormat;
    
    @NotNull
    private Integer recordCount;
    
    @NotNull
    private BigDecimal totalAmount;
    
    @NotNull
    private String currency;
    
    @NotNull
    private LocalDate paymentDate;
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public PayrollFileFormat getFileFormat() { return fileFormat; }
    public void setFileFormat(PayrollFileFormat fileFormat) { this.fileFormat = fileFormat; }
    public Integer getRecordCount() { return recordCount; }
    public void setRecordCount(Integer recordCount) { this.recordCount = recordCount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
