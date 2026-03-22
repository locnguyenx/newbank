package com.banking.product.api.dto;

public class ProductVersionDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer versionNumber;
    private String status;

    public ProductVersionDTO() {
    }

    public ProductVersionDTO(Long id, Long productId, String productName, Integer versionNumber, String status) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.versionNumber = versionNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
