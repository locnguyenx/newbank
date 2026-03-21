package com.banking.charges.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TierRequest {

    @NotNull(message = "Tier start is required")
    private Long tierFrom;

    private Long tierTo;

    @NotNull(message = "Rate is required")
    private BigDecimal rate;

    public TierRequest() {
    }

    public TierRequest(Long tierFrom, Long tierTo, BigDecimal rate) {
        this.tierFrom = tierFrom;
        this.tierTo = tierTo;
        this.rate = rate;
    }

    public Long getTierFrom() {
        return tierFrom;
    }

    public void setTierFrom(Long tierFrom) {
        this.tierFrom = tierFrom;
    }

    public Long getTierTo() {
        return tierTo;
    }

    public void setTierTo(Long tierTo) {
        this.tierTo = tierTo;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
