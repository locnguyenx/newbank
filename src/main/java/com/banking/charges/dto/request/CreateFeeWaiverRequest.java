package com.banking.charges.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateFeeWaiverRequest {

    @NotNull(message = "Charge ID is required")
    private Long chargeId;

    @NotBlank(message = "Scope is required")
    private String scope;

    @NotBlank(message = "Reference ID is required")
    private String referenceId;

    @NotNull(message = "Waiver percentage is required")
    @Min(value = 0, message = "Waiver percentage must be at least 0")
    @Max(value = 100, message = "Waiver percentage must be at most 100")
    private Integer waiverPercentage;

    @NotNull(message = "Valid from date is required")
    private LocalDate validFrom;

    private LocalDate validTo;

    public CreateFeeWaiverRequest() {
    }

    public Long getChargeId() {
        return chargeId;
    }

    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
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
}
