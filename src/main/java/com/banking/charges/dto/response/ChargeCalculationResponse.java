package com.banking.charges.dto.response;

import java.math.BigDecimal;

public class ChargeCalculationResponse {

    private BigDecimal baseAmount;
    private BigDecimal waiverAmount;
    private BigDecimal finalAmount;
    private boolean waiverApplied;
    private String waiverId;
    private String ruleApplied;

    public static ChargeCalculationResponse noCharge() {
        ChargeCalculationResponse response = new ChargeCalculationResponse();
        response.baseAmount = BigDecimal.ZERO;
        response.waiverAmount = BigDecimal.ZERO;
        response.finalAmount = BigDecimal.ZERO;
        response.waiverApplied = false;
        response.waiverId = null;
        response.ruleApplied = null;
        return response;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getWaiverAmount() {
        return waiverAmount;
    }

    public void setWaiverAmount(BigDecimal waiverAmount) {
        this.waiverAmount = waiverAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public boolean isWaiverApplied() {
        return waiverApplied;
    }

    public void setWaiverApplied(boolean waiverApplied) {
        this.waiverApplied = waiverApplied;
    }

    public String getWaiverId() {
        return waiverId;
    }

    public void setWaiverId(String waiverId) {
        this.waiverId = waiverId;
    }

    public String getRuleApplied() {
        return ruleApplied;
    }

    public void setRuleApplied(String ruleApplied) {
        this.ruleApplied = ruleApplied;
    }
}
