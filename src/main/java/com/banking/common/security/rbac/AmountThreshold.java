package com.banking.common.security.rbac;

import com.banking.customer.domain.embeddable.AuditFields;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "amount_thresholds")
@EntityListeners(AuditingEntityListener.class)
public class AmountThreshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal threshold;

    @Embedded
    private AuditFields auditFields = new AuditFields("system");

    public AmountThreshold() {
    }

    public AmountThreshold(Long userId, Role role, BigDecimal threshold) {
        this.userId = userId;
        this.role = role;
        this.threshold = threshold;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public AuditFields getAuditFields() {
        return auditFields;
    }

    public void setAuditFields(AuditFields auditFields) {
        this.auditFields = auditFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountThreshold that = (AmountThreshold) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}