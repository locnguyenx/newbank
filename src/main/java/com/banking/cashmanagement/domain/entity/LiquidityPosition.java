package com.banking.cashmanagement.domain.entity;

import com.banking.cashmanagement.domain.embeddable.AuditFields;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "liquidity_position")
public class LiquidityPosition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "calculation_date_time", nullable = false)
    private LocalDateTime calculationDateTime;
    
    @Column(name = "total_position", precision = 19, scale = 4)
    private BigDecimal totalPosition;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "account_breakdown", columnDefinition = "TEXT")
    private String accountBreakdown;
    
    @Column(name = "available_liquidity", precision = 19, scale = 4)
    private BigDecimal availableLiquidity;
    
    @Column(name = "projected_liquidity", columnDefinition = "TEXT")
    private String projectedLiquidity;
    
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
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public LocalDateTime getCalculationDateTime() { return calculationDateTime; }
    public void setCalculationDateTime(LocalDateTime calculationDateTime) { this.calculationDateTime = calculationDateTime; }
    public BigDecimal getTotalPosition() { return totalPosition; }
    public void setTotalPosition(BigDecimal totalPosition) { this.totalPosition = totalPosition; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getAccountBreakdown() { return accountBreakdown; }
    public void setAccountBreakdown(String accountBreakdown) { this.accountBreakdown = accountBreakdown; }
    public BigDecimal getAvailableLiquidity() { return availableLiquidity; }
    public void setAvailableLiquidity(BigDecimal availableLiquidity) { this.availableLiquidity = availableLiquidity; }
    public String getProjectedLiquidity() { return projectedLiquidity; }
    public void setProjectedLiquidity(String projectedLiquidity) { this.projectedLiquidity = projectedLiquidity; }
    public AuditFields getAuditFields() { return auditFields; }
    public void setAuditFields(AuditFields auditFields) { this.auditFields = auditFields; }
}
