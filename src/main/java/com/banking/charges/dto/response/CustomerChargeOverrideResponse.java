package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.CustomerChargeOverride;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerChargeOverrideResponse {

    private Long id;
    private Long chargeDefinitionId;
    private String chargeName;
    private Long customerId;
    private BigDecimal overrideAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomerChargeOverrideResponse() {
    }

    public static CustomerChargeOverrideResponse fromEntity(CustomerChargeOverride entity) {
        CustomerChargeOverrideResponse response = new CustomerChargeOverrideResponse();
        response.id = entity.getId();
        response.chargeDefinitionId = entity.getChargeDefinition().getId();
        response.chargeName = entity.getChargeDefinition().getName();
        response.customerId = entity.getCustomerId();
        response.overrideAmount = entity.getOverrideAmount();
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

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOverrideAmount() {
        return overrideAmount;
    }

    public void setOverrideAmount(BigDecimal overrideAmount) {
        this.overrideAmount = overrideAmount;
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
