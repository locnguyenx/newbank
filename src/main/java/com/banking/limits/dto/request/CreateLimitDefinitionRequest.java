package com.banking.limits.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CreateLimitDefinitionRequest {

    @NotBlank(message = "Limit name is required")
    private String name;

    @NotBlank(message = "Limit type is required")
    private String limitType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0001", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currency;

    public CreateLimitDefinitionRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
