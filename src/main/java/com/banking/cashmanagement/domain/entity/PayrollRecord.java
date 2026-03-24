package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import com.banking.cashmanagement.domain.enums.PayrollRecordStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payroll_record")
public class PayrollRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "payroll_batch_id", nullable = false)
    private Long payrollBatchId;
    
    @Column(name = "employee_identifier")
    private String employeeIdentifier;
    
    @Column(name = "employee_name")
    private String employeeName;
    
    @Column(name = "bank_account_number")
    private String bankAccountNumber;
    
    @Column(name = "bank_code")
    private String bankCode;
    
    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayrollRecordStatus status;
    
    @Column(name = "error_code")
    private String errorCode;
    
    @Column(name = "error_description")
    private String errorDescription;
    
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
    public Long getPayrollBatchId() { return payrollBatchId; }
    public void setPayrollBatchId(Long payrollBatchId) { this.payrollBatchId = payrollBatchId; }
    public String getEmployeeIdentifier() { return employeeIdentifier; }
    public void setEmployeeIdentifier(String employeeIdentifier) { this.employeeIdentifier = employeeIdentifier; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public PayrollRecordStatus getStatus() { return status; }
    public void setStatus(PayrollRecordStatus status) { this.status = status; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorDescription() { return errorDescription; }
    public void setErrorDescription(String errorDescription) { this.errorDescription = errorDescription; }
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
