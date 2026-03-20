package com.banking.product.domain.entity;

import com.banking.product.domain.embeddable.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "product_features", uniqueConstraints = {
    @UniqueConstraint(name = "uk_product_feature_version_key", columnNames = {"product_version_id", "feature_key"})
})
public class ProductFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Column(name = "feature_key", nullable = false, length = 100)
    private String featureKey;

    @Column(name = "feature_value", nullable = false, length = 255)
    private String featureValue;

    @Embedded
    private AuditFields audit;

    protected ProductFeature() {
    }

    public ProductFeature(ProductVersion productVersion, String featureKey, String featureValue) {
        this.productVersion = productVersion;
        this.featureKey = featureKey;
        this.featureValue = featureValue;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public ProductVersion getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(ProductVersion productVersion) {
        this.productVersion = productVersion;
    }

    public String getFeatureKey() {
        return featureKey;
    }

    public void setFeatureKey(String featureKey) {
        this.featureKey = featureKey;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
