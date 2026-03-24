package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import com.banking.cashmanagement.domain.enums.InvoiceStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receivable_invoice")
public class ReceivableInvoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "bill_to_customer_id")
    private Long billToCustomerId;
    
    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "issue_date")
    private LocalDate issueDate;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;
    
    @Column(name = "balance_due", precision = 19, scale = 4)
    private BigDecimal balanceDue;
    
    @Column(name = "reference_number")
    private String referenceNumber;
    
    @Column(name = "description")
    private String description;
    
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
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
