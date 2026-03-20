package com.banking.product.domain.entity;

import com.banking.product.domain.embeddable.AuditFields;
import com.banking.product.domain.enums.ProductStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_versions", uniqueConstraints = {
    @UniqueConstraint(name = "uk_product_version_number", columnNames = {"product_id", "version_number"})
})
public class ProductVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductStatus status;

    @Column(name = "submitted_by", length = 100)
    private String submittedBy;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "rejection_comment", columnDefinition = "TEXT")
    private String rejectionComment;

    @Column(name = "contract_count")
    private Integer contractCount;

    @Embedded
    private AuditFields audit;

    @OneToMany(mappedBy = "productVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeature> features = new ArrayList<>();

    @OneToMany(mappedBy = "productVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeeEntry> feeEntries = new ArrayList<>();

    @OneToMany(mappedBy = "productVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCustomerSegment> customerSegments = new ArrayList<>();

    protected ProductVersion() {
    }

    public ProductVersion(Product product, Integer versionNumber, ProductStatus status) {
        this.product = product;
        this.versionNumber = versionNumber;
        this.status = status;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
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

    public Integer getContractCount() {
        return contractCount;
    }

    public void setContractCount(Integer contractCount) {
        this.contractCount = contractCount;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }

    public List<ProductFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<ProductFeature> features) {
        this.features = features;
    }

    public List<ProductFeeEntry> getFeeEntries() {
        return feeEntries;
    }

    public void setFeeEntries(List<ProductFeeEntry> feeEntries) {
        this.feeEntries = feeEntries;
    }

    public List<ProductCustomerSegment> getCustomerSegments() {
        return customerSegments;
    }

    public void setCustomerSegments(List<ProductCustomerSegment> customerSegments) {
        this.customerSegments = customerSegments;
    }
}
