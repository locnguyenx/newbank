package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.enums.ProductStatus;
import java.time.LocalDateTime;

public class ProductVersionResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer versionNumber;
    private String status;
    private String submittedBy;
    private String approvedBy;
    private String rejectionComment;
    private Long contractCount;
    private LocalDateTime createdAt;

    public ProductVersionResponse() {
    }

    public static ProductVersionResponse fromEntity(ProductVersion version) {
        ProductVersionResponse response = new ProductVersionResponse();
        response.id = version.getId();
        response.productId = version.getProduct() != null ? version.getProduct().getId() : null;
        response.productName = version.getProduct() != null ? version.getProduct().getName() : null;
        response.versionNumber = version.getVersionNumber();
        response.status = version.getStatus() != null ? version.getStatus().name() : null;
        response.submittedBy = version.getSubmittedBy();
        response.approvedBy = version.getApprovedBy();
        response.rejectionComment = version.getRejectionComment();
        response.contractCount = version.getContractCount();
        if (version.getAudit() != null) {
            response.createdAt = version.getAudit().getCreatedAt();
        }
        return response;
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

    public Long getContractCount() {
        return contractCount;
    }

    public void setContractCount(Long contractCount) {
        this.contractCount = contractCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}