package com.banking.account.domain.entity;

import com.banking.account.domain.embeddable.AuditFields;
import com.banking.account.domain.enums.AccountHolderRole;
import com.banking.account.domain.enums.AccountHolderStatus;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "account_holders", indexes = {
    @Index(name = "idx_account_holders_account_id", columnList = "account_id"),
    @Index(name = "idx_account_holders_customer_id", columnList = "customer_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_account_holder_account_customer", columnNames = {"account_id", "customer_id"})
})
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountHolderRole role;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountHolderStatus status;

    @Embedded
    private AuditFields auditFields;

    protected AccountHolder() {
    }

    public AccountHolder(Account account, Long customerId, AccountHolderRole role,
                          LocalDate effectiveFrom, AccountHolderStatus status) {
        this.account = account;
        this.customerId = customerId;
        this.role = role;
        this.effectiveFrom = effectiveFrom;
        this.status = status;
        this.auditFields = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public AccountHolderRole getRole() {
        return role;
    }

    public void setRole(AccountHolderRole role) {
        this.role = role;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public AccountHolderStatus getStatus() {
        return status;
    }

    public void setStatus(AccountHolderStatus status) {
        this.status = status;
    }

    public AuditFields getAuditFields() {
        return auditFields;
    }

    public void setAuditFields(AuditFields auditFields) {
        this.auditFields = auditFields;
    }
}
