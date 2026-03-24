package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import com.banking.cashmanagement.domain.enums.PaymentInstructionStatus;
import com.banking.cashmanagement.domain.enums.PaymentType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "batch_payment_instruction")
public class BatchPaymentInstruction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "batch_payment_id", nullable = false)
    private Long batchPaymentId;
    
    @Column(name = "instruction_reference")
    private String instructionReference;
    
    @Column(name = "beneficiary_name")
    private String beneficiaryName;
    
    @Column(name = "beneficiary_account")
    private String beneficiaryAccount;
    
    @Column(name = "beneficiary_bank_code")
    private String beneficiaryBankCode;
    
    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentInstructionStatus status;
    
    @Column(name = "error_code")
    private String errorCode;
    
    @Column(name = "error_description")
    private String errorDescription;
    
    @Column(name = "settlement_date")
    private LocalDate settlementDate;
    
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
    public Long getBatchPaymentId() { return batchPaymentId; }
    public void setBatchPaymentId(Long batchPaymentId) { this.batchPaymentId = batchPaymentId; }
    public String getInstructionReference() { return instructionReference; }
    public void setInstructionReference(String instructionReference) { this.instructionReference = instructionReference; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public String getBeneficiaryAccount() { return beneficiaryAccount; }
    public void setBeneficiaryAccount(String beneficiaryAccount) { this.beneficiaryAccount = beneficiaryAccount; }
    public String getBeneficiaryBankCode() { return beneficiaryBankCode; }
    public void setBeneficiaryBankCode(String beneficiaryBankCode) { this.beneficiaryBankCode = beneficiaryBankCode; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }
    public PaymentInstructionStatus getStatus() { return status; }
    public void setStatus(PaymentInstructionStatus status) { this.status = status; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getErrorDescription() { return errorDescription; }
    public void setErrorDescription(String errorDescription) { this.errorDescription = errorDescription; }
    public LocalDate getSettlementDate() { return settlementDate; }
    public void setSettlementDate(LocalDate settlementDate) { this.settlementDate = settlementDate; }
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
