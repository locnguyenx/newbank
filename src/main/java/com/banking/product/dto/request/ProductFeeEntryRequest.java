package com.banking.product.dto.request;

import com.banking.product.domain.enums.FeeCalculationMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ProductFeeEntryRequest {

    @NotBlank(message = "Fee type is required")
    private String feeType;

    @NotNull(message = "Calculation method is required")
    private FeeCalculationMethod calculationMethod;

    private BigDecimal amount;

    private BigDecimal rate;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, message = "Currency code must not exceed 3 characters")
    private String currency;

    @Valid
    private List<ProductFeeTierRequest> tiers;

    public ProductFeeEntryRequest() {
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public FeeCalculationMethod getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(FeeCalculationMethod calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<ProductFeeTierRequest> getTiers() {
        return tiers;
    }

    public void setTiers(List<ProductFeeTierRequest> tiers) {
        this.tiers = tiers;
    }
}