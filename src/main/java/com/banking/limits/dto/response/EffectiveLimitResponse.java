package com.banking.limits.dto.response;

import java.math.BigDecimal;

public class EffectiveLimitResponse {

    private Long limitDefinitionId;
    private String limitName;
    private String limitType;
    private BigDecimal effectiveLimit;
    private String source;
    private String currency;
    private String status;

    public EffectiveLimitResponse() {
    }

    public static EffectiveLimitResponse fromAccount(Long limitDefinitionId, String limitName, String limitType, 
                                                      BigDecimal effectiveLimit, String currency, String status) {
        EffectiveLimitResponse response = new EffectiveLimitResponse();
        response.limitDefinitionId = limitDefinitionId;
        response.limitName = limitName;
        response.limitType = limitType;
        response.effectiveLimit = effectiveLimit;
        response.source = "ACCOUNT";
        response.currency = currency;
        response.status = status;
        return response;
    }

    public static EffectiveLimitResponse fromCustomer(Long limitDefinitionId, String limitName, String limitType,
                                                      BigDecimal effectiveLimit, String currency, String status) {
        EffectiveLimitResponse response = new EffectiveLimitResponse();
        response.limitDefinitionId = limitDefinitionId;
        response.limitName = limitName;
        response.limitType = limitType;
        response.effectiveLimit = effectiveLimit;
        response.source = "CUSTOMER";
        response.currency = currency;
        response.status = status;
        return response;
    }

    public static EffectiveLimitResponse fromProduct(Long limitDefinitionId, String limitName, String limitType,
                                                      BigDecimal effectiveLimit, String currency, String status) {
        EffectiveLimitResponse response = new EffectiveLimitResponse();
        response.limitDefinitionId = limitDefinitionId;
        response.limitName = limitName;
        response.limitType = limitType;
        response.effectiveLimit = effectiveLimit;
        response.source = "PRODUCT";
        response.currency = currency;
        response.status = status;
        return response;
    }

    public Long getLimitDefinitionId() {
        return limitDefinitionId;
    }

    public void setLimitDefinitionId(Long limitDefinitionId) {
        this.limitDefinitionId = limitDefinitionId;
    }

    public String getLimitName() {
        return limitName;
    }

    public void setLimitName(String limitName) {
        this.limitName = limitName;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public BigDecimal getEffectiveLimit() {
        return effectiveLimit;
    }

    public void setEffectiveLimit(BigDecimal effectiveLimit) {
        this.effectiveLimit = effectiveLimit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
}