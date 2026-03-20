package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductFeeTier;
import java.math.BigDecimal;

public class ProductFeeTierResponse {

    private Long id;
    private Long tierFrom;
    private Long tierTo;
    private BigDecimal rate;

    public ProductFeeTierResponse() {
    }

    public static ProductFeeTierResponse fromEntity(ProductFeeTier tier) {
        ProductFeeTierResponse response = new ProductFeeTierResponse();
        response.id = tier.getId();
        response.tierFrom = tier.getTierFrom();
        response.tierTo = tier.getTierTo();
        response.rate = tier.getRate();
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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