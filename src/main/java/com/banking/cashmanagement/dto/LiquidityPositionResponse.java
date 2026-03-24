package com.banking.cashmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LiquidityPositionResponse {
    
    private Long id;
    private Long customerId;
    private LocalDateTime calculationDateTime;
    private BigDecimal totalPosition;
    private String currency;
    private String accountBreakdown;
    private BigDecimal availableLiquidity;
    private String projectedLiquidity;
    
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
}
