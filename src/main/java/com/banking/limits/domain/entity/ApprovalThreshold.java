package com.banking.limits.domain.entity;

import com.banking.limits.domain.embeddable.AuditFields;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "approval_thresholds", uniqueConstraints = @UniqueConstraint(columnNames = {"limit_definition_id"}))
public class ApprovalThreshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false, unique = true)
    private LimitDefinition limitDefinition;

    @Column(name = "absolute_amount", precision = 19, scale = 4)
    private BigDecimal absoluteAmount;

    @Column(name = "percentage_of_limit")
    private Integer percentageOfLimit;

    @Embedded
    private AuditFields audit;

    protected ApprovalThreshold() {
    }

    public ApprovalThreshold(LimitDefinition limitDefinition, BigDecimal absoluteAmount, Integer percentageOfLimit) {
        this.limitDefinition = limitDefinition;
        this.absoluteAmount = absoluteAmount;
        this.percentageOfLimit = percentageOfLimit;
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

    public BigDecimal getAbsoluteAmount() {
        return absoluteAmount;
    }

    public void setAbsoluteAmount(BigDecimal absoluteAmount) {
        this.absoluteAmount = absoluteAmount;
    }

    public Integer getPercentageOfLimit() {
        return percentageOfLimit;
    }

    public void setPercentageOfLimit(Integer percentageOfLimit) {
        this.percentageOfLimit = percentageOfLimit;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
