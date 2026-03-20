package com.banking.product.domain.entity;

import com.banking.customer.domain.enums.CustomerType;
import com.banking.product.domain.embeddable.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "product_customer_segments", uniqueConstraints = {
    @UniqueConstraint(name = "uk_product_segment_version_type", columnNames = {"product_version_id", "customer_type"})
})
public class ProductCustomerSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, length = 30)
    private CustomerType customerType;

    @Embedded
    private AuditFields audit;

    protected ProductCustomerSegment() {
    }

    public ProductCustomerSegment(ProductVersion productVersion, CustomerType customerType) {
        this.productVersion = productVersion;
        this.customerType = customerType;
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

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
