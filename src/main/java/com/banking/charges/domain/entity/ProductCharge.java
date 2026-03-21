package com.banking.charges.domain.entity;

import com.banking.charges.domain.embeddable.AuditFields;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_charges", uniqueConstraints = @UniqueConstraint(columnNames = {"charge_definition_id", "product_code"}))
public class ProductCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;

    @Column(name = "override_amount", precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;

    protected ProductCharge() {
    }

    public ProductCharge(ChargeDefinition chargeDefinition, String productCode, BigDecimal overrideAmount) {
        this.chargeDefinition = chargeDefinition;
        this.productCode = productCode;
        this.overrideAmount = overrideAmount;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChargeDefinition getChargeDefinition() {
        return chargeDefinition;
    }

    public void setChargeDefinition(ChargeDefinition chargeDefinition) {
        this.chargeDefinition = chargeDefinition;
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
