package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductVersion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDetailResponse {

    private Long id;
    private String code;
    private String name;
    private String family;
    private String status;
    private Long versionNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<ProductFeatureResponse> features;
    private List<ProductFeeEntryResponse> feeEntries;
    private List<ProductCustomerSegmentResponse> customerSegments;

    public ProductDetailResponse() {
    }

    public static ProductDetailResponse fromEntity(ProductVersion version) {
        ProductDetailResponse response = new ProductDetailResponse();
        
        if (version.getProduct() != null) {
            response.id = version.getProduct().getId();
            response.code = version.getProduct().getCode();
            response.name = version.getProduct().getName();
            response.family = version.getProduct().getFamily() != null 
                ? version.getProduct().getFamily().name() : null;
            response.versionNumber = (long) version.getVersionNumber();
            if (version.getProduct().getAudit() != null) {
                response.createdAt = version.getProduct().getAudit().getCreatedAt();
                response.updatedAt = version.getProduct().getAudit().getUpdatedAt();
                response.createdBy = version.getProduct().getAudit().getCreatedBy();
                response.updatedBy = version.getProduct().getAudit().getUpdatedBy();
            }
        }
        
        response.status = version.getStatus() != null ? version.getStatus().name() : null;
        
        if (version.getFeatures() != null) {
            response.features = version.getFeatures().stream()
                .map(ProductFeatureResponse::fromEntity)
                .collect(Collectors.toList());
        }
        
        if (version.getFeeEntries() != null) {
            response.feeEntries = version.getFeeEntries().stream()
                .map(ProductFeeEntryResponse::fromEntity)
                .collect(Collectors.toList());
        }
        
        if (version.getCustomerSegments() != null) {
            response.customerSegments = version.getCustomerSegments().stream()
                .map(ProductCustomerSegmentResponse::fromEntity)
                .collect(Collectors.toList());
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<ProductFeatureResponse> getFeatures() {
        return features;
    }

    public void setFeatures(List<ProductFeatureResponse> features) {
        this.features = features;
    }

    public List<ProductFeeEntryResponse> getFeeEntries() {
        return feeEntries;
    }

    public void setFeeEntries(List<ProductFeeEntryResponse> feeEntries) {
        this.feeEntries = feeEntries;
    }

    public List<ProductCustomerSegmentResponse> getCustomerSegments() {
        return customerSegments;
    }

    public void setCustomerSegments(List<ProductCustomerSegmentResponse> customerSegments) {
        this.customerSegments = customerSegments;
    }
}