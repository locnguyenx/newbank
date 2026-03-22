package com.banking.charges.api.dto;

import java.math.BigDecimal;

public class ChargeResult {
    private String chargeType;
    private BigDecimal amount;
    private String currency;
    private String calculationMethod;

    public ChargeResult() {
    }

    public ChargeResult(String chargeType, BigDecimal amount, String currency, String calculationMethod) {
        this.chargeType = chargeType;
        this.amount = amount;
        this.currency = currency;
        this.calculationMethod = calculationMethod;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }
}
