package com.banking.limits.domain.entity;

import com.banking.limits.domain.embeddable.AuditFields;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_limits", uniqueConstraints = @UniqueConstraint(columnNames = {"limit_definition_id", "product_code"}))
public class ProductLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;

    @Column(name = "override_amount", precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;

    protected ProductLimit() {
    }

    public ProductLimit(LimitDefinition limitDefinition, String productCode, BigDecimal overrideAmount) {
        this.limitDefinition = limitDefinition;
        this.productCode = productCode;
        this.overrideAmount = overrideAmount;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public LimitDefinition getLimitDefinition() {
        return limitDefinition;
    }

    public void setLimitDefinition(LimitDefinition limitDefinition) {
        this.limitDefinition = limitDefinition;
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

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
