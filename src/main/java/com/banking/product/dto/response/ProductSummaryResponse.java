package com.banking.product.dto.response;

import com.banking.product.domain.entity.Product;

public class ProductSummaryResponse {

    private Long id;
    private String code;
    private String name;
    private String family;
    private String status;
    private Integer currentVersionNumber;

    public ProductSummaryResponse() {
    }

    public static ProductSummaryResponse fromEntity(Product product) {
        ProductSummaryResponse response = new ProductSummaryResponse();
        response.id = product.getId();
        response.code = product.getCode();
        response.name = product.getName();
        response.family = product.getFamily() != null ? product.getFamily().name() : null;
        response.currentVersionNumber = product.getId() != null ? product.getId().intValue() : null;
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

    public Integer getCurrentVersionNumber() {
        return currentVersionNumber;
    }

    public void setCurrentVersionNumber(Integer currentVersionNumber) {
        this.currentVersionNumber = currentVersionNumber;
    }
}