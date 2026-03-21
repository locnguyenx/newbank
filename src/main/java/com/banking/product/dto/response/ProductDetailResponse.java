package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductVersion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDetailResponse {

    private ProductInfo product;
    private Long id;
    private Long versionNumber;
    private String status;
    private Integer contractCount;
    private String submittedBy;
    private String approvedBy;
    private String rejectionComment;
    private LocalDateTime createdAt;
    private String createdBy;
    private List<ProductFeatureResponse> features;
    private List<ProductFeeEntryResponse> feeEntries;
    private List<SegmentInfo> segments;

    public ProductDetailResponse() {
    }

    public static class ProductInfo {
        private Long id;
        private String code;
        private String name;
        private String family;
        private String description;

        public ProductInfo() {
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class SegmentInfo {
        private String customerType;

        public SegmentInfo() {
        }

        public SegmentInfo(String customerType) {
            this.customerType = customerType;
        }

        public String getCustomerType() {
            return customerType;
        }

        public void setCustomerType(String customerType) {
            this.customerType = customerType;
        }
    }

    public static ProductDetailResponse fromEntity(ProductVersion version) {
        ProductDetailResponse response = new ProductDetailResponse();

        response.id = version.getId();
        response.versionNumber = (long) version.getVersionNumber();
        response.status = version.getStatus() != null ? version.getStatus().name() : null;
        response.submittedBy = version.getSubmittedBy();
        response.approvedBy = version.getApprovedBy();
        response.rejectionComment = version.getRejectionComment();
        response.contractCount = version.getContractCount();

        if (version.getProduct() != null) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(version.getProduct().getId());
            productInfo.setCode(version.getProduct().getCode());
            productInfo.setName(version.getProduct().getName());
            productInfo.setFamily(version.getProduct().getFamily() != null
                ? version.getProduct().getFamily().name() : null);
            productInfo.setDescription(version.getProduct().getDescription());
            response.product = productInfo;
            if (version.getProduct().getAudit() != null) {
                response.createdAt = version.getProduct().getAudit().getCreatedAt();
                response.createdBy = version.getProduct().getAudit().getCreatedBy();
            }
        }

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
            response.segments = version.getCustomerSegments().stream()
                .map(s -> new SegmentInfo(s.getCustomerType() != null ? s.getCustomerType().name() : null))
                .collect(Collectors.toList());
        }

        return response;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getContractCount() {
        return contractCount;
    }

    public void setContractCount(Integer contractCount) {
        this.contractCount = contractCount;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectionComment() {
        return rejectionComment;
    }

    public void setRejectionComment(String rejectionComment) {
        this.rejectionComment = rejectionComment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public List<SegmentInfo> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentInfo> segments) {
        this.segments = segments;
    }
}
