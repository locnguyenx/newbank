package com.banking.limits.domain.entity;

import com.banking.limits.domain.embeddable.AuditFields;
import com.banking.limits.domain.enums.LimitStatus;
import com.banking.limits.domain.enums.LimitType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "limit_definitions")
public class LimitDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "limit_type", nullable = false)
    private LimitType limitType;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LimitStatus status = LimitStatus.ACTIVE;

    @Embedded
    private AuditFields audit;

    protected LimitDefinition() {
    }

    public LimitDefinition(String name, LimitType limitType, BigDecimal amount, String currency) {
        this.name = name;
        this.limitType = limitType;
        this.amount = amount;
        this.currency = currency;
        this.status = LimitStatus.ACTIVE;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LimitStatus getStatus() {
        return status;
    }

    public void setStatus(LimitStatus status) {
        this.status = status;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
