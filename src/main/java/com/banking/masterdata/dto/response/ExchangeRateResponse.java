package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExchangeRateResponse {

    private Long id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
    private LocalDate effectiveDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExchangeRateResponse() {
    }

    public static ExchangeRateResponse fromEntity(ExchangeRate exchangeRate) {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.id = exchangeRate.getId();
        response.baseCurrencyCode = exchangeRate.getBaseCurrency().getCode();
        response.targetCurrencyCode = exchangeRate.getTargetCurrency().getCode();
        response.rate = exchangeRate.getRate();
        response.effectiveDate = exchangeRate.getEffectiveDate();
        if (exchangeRate.getAudit() != null) {
            response.createdAt = exchangeRate.getAudit().getCreatedAt();
            response.updatedAt = exchangeRate.getAudit().getUpdatedAt();
        }
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
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
