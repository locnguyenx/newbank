package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductFeeEntry;
import com.banking.product.domain.entity.ProductFeeTier;
import com.banking.product.domain.enums.FeeCalculationMethod;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class ProductFeeEntryResponse {

    private Long id;
    private String feeType;
    private String calculationMethod;
    private BigDecimal amount;
    private BigDecimal rate;
    private String currency;
    private List<ProductFeeTierResponse> tiers;

    public ProductFeeEntryResponse() {
    }

    public static ProductFeeEntryResponse fromEntity(ProductFeeEntry feeEntry) {
        ProductFeeEntryResponse response = new ProductFeeEntryResponse();
        response.id = feeEntry.getId();
        response.feeType = feeEntry.getFeeType();
        response.calculationMethod = feeEntry.getCalculationMethod() != null 
            ? feeEntry.getCalculationMethod().name() : null;
        response.amount = feeEntry.getAmount();
        response.rate = feeEntry.getRate();
        response.currency = feeEntry.getCurrency();
        if (feeEntry.getTiers() != null) {
            response.tiers = feeEntry.getTiers().stream()
                .map(ProductFeeTierResponse::fromEntity)
                .collect(Collectors.toList());
        }
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
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

    public List<ProductFeeTierResponse> getTiers() {
        return tiers;
    }

    public void setTiers(List<ProductFeeTierResponse> tiers) {
        this.tiers = tiers;
    }
}