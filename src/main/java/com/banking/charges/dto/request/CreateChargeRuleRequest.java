package com.banking.charges.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

public class CreateChargeRuleRequest {

    @NotBlank(message = "Calculation method is required")
    private String calculationMethod;

    private BigDecimal flatAmount;
    private BigDecimal percentageRate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private List<TierRequest> tiers;

    public CreateChargeRuleRequest() {
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public BigDecimal getFlatAmount() {
        return flatAmount;
    }

    public void setFlatAmount(BigDecimal flatAmount) {
        this.flatAmount = flatAmount;
    }

    public BigDecimal getPercentageRate() {
        return percentageRate;
    }

    public void setPercentageRate(BigDecimal percentageRate) {
        this.percentageRate = percentageRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<TierRequest> getTiers() {
        return tiers;
    }

    public void setTiers(List<TierRequest> tiers) {
        this.tiers = tiers;
    }
}
