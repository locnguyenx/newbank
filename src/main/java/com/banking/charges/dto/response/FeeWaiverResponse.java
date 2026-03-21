package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.FeeWaiver;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FeeWaiverResponse {

    private Long id;
    private Long chargeDefinitionId;
    private String chargeName;
    private String scope;
    private String referenceId;
    private Integer waiverPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FeeWaiverResponse() {
    }

    public static FeeWaiverResponse fromEntity(FeeWaiver entity) {
        FeeWaiverResponse response = new FeeWaiverResponse();
        response.id = entity.getId();
        response.chargeDefinitionId = entity.getChargeDefinition().getId();
        response.chargeName = entity.getChargeDefinition().getName();
        response.scope = entity.getScope().name();
        response.referenceId = entity.getReferenceId();
        response.waiverPercentage = entity.getWaiverPercentage();
        response.validFrom = entity.getValidFrom();
        response.validTo = entity.getValidTo();
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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getWaiverPercentage() {
        return waiverPercentage;
    }

    public void setWaiverPercentage(Integer waiverPercentage) {
        this.waiverPercentage = waiverPercentage;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
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
