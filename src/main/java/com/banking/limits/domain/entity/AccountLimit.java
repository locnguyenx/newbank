package com.banking.limits.domain.entity;

import com.banking.limits.domain.embeddable.AuditFields;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account_limits", uniqueConstraints = @UniqueConstraint(columnNames = {"limit_definition_id", "account_number"}))
public class AccountLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "account_number", nullable = false, length = 30)
    private String accountNumber;

    @Column(name = "override_amount", precision = 19, scale = 4)
    private BigDecimal overrideAmount;

    @Embedded
    private AuditFields audit;

    protected AccountLimit() {
    }

    public AccountLimit(LimitDefinition limitDefinition, String accountNumber, BigDecimal overrideAmount) {
        this.limitDefinition = limitDefinition;
        this.accountNumber = accountNumber;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
