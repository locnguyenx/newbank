package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.InterestRate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InterestRateResponse {

    private Long id;
    private Long chargeDefinitionId;
    private String productCode;
    private BigDecimal fixedRate;
    private String accrualSchedule;
    private String applicationSchedule;
    private List<InterestTierResponse> tiers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InterestRateResponse fromEntity(InterestRate entity) {
        InterestRateResponse response = new InterestRateResponse();
        response.id = entity.getId();
        response.chargeDefinitionId = entity.getChargeDefinition().getId();
        response.productCode = entity.getProductCode();
        response.fixedRate = entity.getFixedRate();
        response.accrualSchedule = entity.getAccrualSchedule().name();
        response.applicationSchedule = entity.getApplicationSchedule().name();
        response.createdAt = entity.getAudit().getCreatedAt();
        response.updatedAt = entity.getAudit().getUpdatedAt();
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public String getAccrualSchedule() {
        return accrualSchedule;
    }

    public void setAccrualSchedule(String accrualSchedule) {
        this.accrualSchedule = accrualSchedule;
    }

    public String getApplicationSchedule() {
        return applicationSchedule;
    }

    public void setApplicationSchedule(String applicationSchedule) {
        this.applicationSchedule = applicationSchedule;
    }

    public List<InterestTierResponse> getTiers() {
        return tiers;
    }

    public void setTiers(List<InterestTierResponse> tiers) {
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
