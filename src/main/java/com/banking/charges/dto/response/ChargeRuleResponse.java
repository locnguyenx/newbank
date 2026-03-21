package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.ChargeRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ChargeRuleResponse {

    private Long id;
    private Long chargeDefinitionId;
    private String calculationMethod;
    private BigDecimal flatAmount;
    private BigDecimal percentageRate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private List<ChargeTierResponse> tiers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChargeRuleResponse() {
    }

    public static ChargeRuleResponse fromEntity(ChargeRule entity) {
        ChargeRuleResponse response = new ChargeRuleResponse();
        response.id = entity.getId();
        response.chargeDefinitionId = entity.getChargeDefinition().getId();
        response.calculationMethod = entity.getMethod().name();
        response.flatAmount = entity.getFlatAmount();
        response.percentageRate = entity.getPercentageRate();
        response.minAmount = entity.getMinAmount();
        response.maxAmount = entity.getMaxAmount();
        response.tiers = entity.getTiers().stream()
                .map(ChargeTierResponse::fromEntity)
                .toList();
        if (entity.getAudit() != null) {
            response.createdAt = entity.getAudit().getCreatedAt();
            response.updatedAt = entity.getAudit().getUpdatedAt();
        }
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChargeDefinitionId() {
        return chargeDefinitionId;
    }

    public void setChargeDefinitionId(Long chargeDefinitionId) {
        this.chargeDefinitionId = chargeDefinitionId;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public BigDecimal getFlatAmount() {
        return flatAmount;
    }

    public void setFlatAmount(BigDecimal flatAmount) {
        this.flatAmount = flatAmount;
    }

    public BigDecimal getPercentageRate() {
        return percentageRate;
    }

    public void setPercentageRate(BigDecimal percentageRate) {
        this.percentageRate = percentageRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<ChargeTierResponse> getTiers() {
        return tiers;
    }

    public void setTiers(List<ChargeTierResponse> tiers) {
        this.tiers = tiers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
