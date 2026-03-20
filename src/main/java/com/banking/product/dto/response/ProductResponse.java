package com.banking.product.dto.response;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;
    private String code;
    private String name;
    private String family;
    private String status;
    private Long versionNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse() {
    }

    public static ProductResponse fromEntity(Product product) {
        ProductResponse response = new ProductResponse();
        response.id = product.getId();
        response.code = product.getCode();
        response.name = product.getName();
        response.family = product.getFamily() != null ? product.getFamily().name() : null;
        response.versionNumber = product.getVersion();
        if (product.getAudit() != null) {
            response.createdAt = product.getAudit().getCreatedAt();
            response.updatedAt = product.getAudit().getUpdatedAt();
        }
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
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