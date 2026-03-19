package com.banking.customer.domain.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class PercentageRate {

    @Column(name = "rate_value")
    private BigDecimal value;

    @Column(name = "rate_basis")
    private Integer basis;

    protected PercentageRate() {
    }

    public PercentageRate(BigDecimal value, Integer basis) {
        this.value = value.setScale(6, RoundingMode.HALF_EVEN);
        this.basis = basis;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Integer getBasis() {
        return basis;
    }

    public BigDecimal toRate() {
        return value.divide(BigDecimal.valueOf(basis), 6, RoundingMode.HALF_EVEN);
    }

    public static PercentageRate fromPercentage(BigDecimal percentage, Integer basis) {
        return new PercentageRate(percentage.multiply(BigDecimal.valueOf(basis)).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_EVEN), basis);
    }
}
