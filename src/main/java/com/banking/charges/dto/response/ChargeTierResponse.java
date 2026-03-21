package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.ChargeTier;

import java.math.BigDecimal;

public class ChargeTierResponse {

    private Long id;
    private Long tierFrom;
    private Long tierTo;
    private BigDecimal rate;

    public ChargeTierResponse() {
    }

    public static ChargeTierResponse fromEntity(ChargeTier entity) {
        ChargeTierResponse response = new ChargeTierResponse();
        response.id = entity.getId();
        response.tierFrom = entity.getTierFrom();
        response.tierTo = entity.getTierTo();
        response.rate = entity.getRate();
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
