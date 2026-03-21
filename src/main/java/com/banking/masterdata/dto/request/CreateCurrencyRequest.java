package com.banking.masterdata.dto.request;

import jakarta.validation.constraints.*;

public class CreateCurrencyRequest {

    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String code;

    @NotBlank(message = "Currency name is required")
    private String name;

    private String symbol;

    @NotNull(message = "Decimal places is required")
    @Min(value = 0, message = "Decimal places must be at least 0")
    @Max(value = 10, message = "Decimal places must not exceed 10")
    private Integer decimalPlaces;

    public CreateCurrencyRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
}
