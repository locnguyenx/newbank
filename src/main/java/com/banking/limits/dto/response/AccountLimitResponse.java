package com.banking.limits.dto.response;

import com.banking.limits.domain.entity.AccountLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountLimitResponse {

    private Long id;
    private Long limitDefinitionId;
    private String limitName;
    private String accountNumber;
    private BigDecimal overrideAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountLimitResponse() {
    }

    public static AccountLimitResponse fromEntity(AccountLimit entity) {
        AccountLimitResponse response = new AccountLimitResponse();
        response.id = entity.getId();
        response.limitDefinitionId = entity.getLimitDefinition().getId();
        response.limitName = entity.getLimitDefinition().getName();
        response.accountNumber = entity.getAccountNumber();
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
