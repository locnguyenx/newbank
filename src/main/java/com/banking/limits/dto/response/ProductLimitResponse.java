package com.banking.limits.dto.response;

import com.banking.limits.domain.entity.ProductLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductLimitResponse {

    private Long id;
    private Long limitDefinitionId;
    private String limitName;
    private String productCode;
    private BigDecimal overrideAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductLimitResponse() {
    }

    public static ProductLimitResponse fromEntity(ProductLimit entity) {
        ProductLimitResponse response = new ProductLimitResponse();
        response.id = entity.getId();
        response.limitDefinitionId = entity.getLimitDefinition().getId();
        response.limitName = entity.getLimitDefinition().getName();
        response.productCode = entity.getProductCode();
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
