package com.banking.limits.domain.entity;

import com.banking.limits.domain.embeddable.AuditFields;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customer_limits", uniqueConstraints = @UniqueConstraint(columnNames = {"limit_definition_id", "customer_id"}))
public class CustomerLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "override_amount", precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;

    protected CustomerLimit() {
    }

    public CustomerLimit(LimitDefinition limitDefinition, Long customerId, BigDecimal overrideAmount) {
        this.limitDefinition = limitDefinition;
        this.customerId = customerId;
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
