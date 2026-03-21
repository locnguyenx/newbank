package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.ChargeDefinition;

import java.time.LocalDateTime;

public class ChargeDefinitionResponse {

    private Long id;
    private String name;
    private String chargeType;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChargeDefinitionResponse() {
    }

    public static ChargeDefinitionResponse fromEntity(ChargeDefinition entity) {
        ChargeDefinitionResponse response = new ChargeDefinitionResponse();
        response.id = entity.getId();
        response.name = entity.getName();
        response.chargeType = entity.getChargeType().name();
        response.currency = entity.getCurrency();
        response.status = entity.getStatus().name();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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