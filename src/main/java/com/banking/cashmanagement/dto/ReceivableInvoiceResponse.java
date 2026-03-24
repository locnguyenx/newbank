package com.banking.cashmanagement.dto;

import com.banking.cashmanagement.domain.enums.InvoiceStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReceivableInvoiceResponse {
    
    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private Long billToCustomerId;
    private BigDecimal amount;
    private String currency;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private InvoiceStatus status;
    private BigDecimal balanceDue;
    private String referenceNumber;
    private String description;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
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
    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
    public BigDecimal getBalanceDue() { return balanceDue; }
    public void setBalanceDue(BigDecimal balanceDue) { this.balanceDue = balanceDue; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
