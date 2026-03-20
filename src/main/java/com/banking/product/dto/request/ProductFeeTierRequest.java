package com.banking.product.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductFeeTierRequest {

    @NotNull(message = "Tier from is required")
    private Long tierFrom;

    private Long tierTo;

    @NotNull(message = "Rate is required")
    private BigDecimal rate;

    public ProductFeeTierRequest() {
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