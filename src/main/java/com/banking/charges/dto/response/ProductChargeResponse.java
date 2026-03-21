package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.ProductCharge;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductChargeResponse {

    private Long id;
    private Long chargeDefinitionId;
    private String chargeName;
    private String productCode;
    private BigDecimal overrideAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductChargeResponse() {
    }

    public static ProductChargeResponse fromEntity(ProductCharge entity) {
        ProductChargeResponse response = new ProductChargeResponse();
        response.id = entity.getId();
        response.chargeDefinitionId = entity.getChargeDefinition().getId();
        response.chargeName = entity.getChargeDefinition().getName();
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
