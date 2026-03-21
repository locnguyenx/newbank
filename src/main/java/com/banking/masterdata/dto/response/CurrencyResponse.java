package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.Currency;

import java.time.LocalDateTime;

public class CurrencyResponse {

    private String code;
    private String name;
    private String symbol;
    private int decimalPlaces;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CurrencyResponse() {
    }

    public static CurrencyResponse fromEntity(Currency currency) {
        CurrencyResponse response = new CurrencyResponse();
        response.code = currency.getCode();
        response.name = currency.getName();
        response.symbol = currency.getSymbol();
        response.decimalPlaces = currency.getDecimalPlaces();
        response.active = currency.isActive();
        if (currency.getAudit() != null) {
            response.createdAt = currency.getAudit().getCreatedAt();
            response.updatedAt = currency.getAudit().getUpdatedAt();
        }
        return response;
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

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
