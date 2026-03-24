package com.banking.cashmanagement.dto;

import com.banking.cashmanagement.domain.enums.BatchFileFormat;
import com.banking.cashmanagement.domain.enums.BatchPaymentStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BatchPaymentRequest {
    
    @NotNull
    private Long customerId;
    
    @NotNull
    private BatchFileFormat fileFormat;
    
    @NotNull
    private Integer instructionCount;
    
    @NotNull
    private BigDecimal totalAmount;
    
    @NotNull
    private String currency;
    
    private LocalDate paymentDate;
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public BatchFileFormat getFileFormat() { return fileFormat; }
    public void setFileFormat(BatchFileFormat fileFormat) { this.fileFormat = fileFormat; }
    public Integer getInstructionCount() { return instructionCount; }
    public void setInstructionCount(Integer instructionCount) { this.instructionCount = instructionCount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
