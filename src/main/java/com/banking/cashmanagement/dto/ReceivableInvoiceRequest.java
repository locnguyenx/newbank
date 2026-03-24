package com.banking.cashmanagement.dto;

import com.banking.cashmanagement.domain.enums.InvoiceStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceivableInvoiceRequest {
    
    @NotNull
    private Long customerId;
    
    private Long billToCustomerId;
    
    @NotNull
    private BigDecimal amount;
    
    @NotNull
    private String currency;
    
    @NotNull
    private LocalDate issueDate;
    
    @NotNull
    private LocalDate dueDate;
    
    private String referenceNumber;
    private String description;
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getBillToCustomerId() { return billToCustomerId; }
    public void setBillToCustomerId(Long billToCustomerId) { this.billToCustomerId = billToCustomerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
