package com.banking.limits.dto.response;

import com.banking.limits.domain.entity.LimitDefinition;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LimitDefinitionResponse {

    private Long id;
    private String name;
    private String limitType;
    private BigDecimal amount;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LimitDefinitionResponse() {
    }

    public static LimitDefinitionResponse fromEntity(LimitDefinition entity) {
        LimitDefinitionResponse response = new LimitDefinitionResponse();
        response.id = entity.getId();
        response.name = entity.getName();
        response.limitType = entity.getLimitType().name();
        response.amount = entity.getAmount();
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
