package com.banking.charges.domain.entity;

import com.banking.charges.domain.embeddable.AuditFields;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customer_charge_overrides", uniqueConstraints = @UniqueConstraint(columnNames = {"charge_definition_id", "customer_id"}))
public class CustomerChargeOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "override_amount", precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;

    protected CustomerChargeOverride() {
    }

    public CustomerChargeOverride(ChargeDefinition chargeDefinition, Long customerId, BigDecimal overrideAmount) {
        this.chargeDefinition = chargeDefinition;
        this.customerId = customerId;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
