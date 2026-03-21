package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.InterestTier;

import java.math.BigDecimal;

public class InterestTierResponse {

    private Long id;
    private BigDecimal balanceFrom;
    private BigDecimal balanceTo;
    private BigDecimal rate;

    public static InterestTierResponse fromEntity(InterestTier entity) {
        InterestTierResponse response = new InterestTierResponse();
        response.id = entity.getId();
        response.balanceFrom = entity.getBalanceFrom();
        response.balanceTo = entity.getBalanceTo();
        response.rate = entity.getRate();
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalanceFrom() {
        return balanceFrom;
    }

    public void setBalanceFrom(BigDecimal balanceFrom) {
        this.balanceFrom = balanceFrom;
    }

    public BigDecimal getBalanceTo() {
        return balanceTo;
    }

    public void setBalanceTo(BigDecimal balanceTo) {
        this.balanceTo = balanceTo;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
