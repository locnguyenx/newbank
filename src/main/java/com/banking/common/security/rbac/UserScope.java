package com.banking.common.security.rbac;

import com.banking.customer.domain.embeddable.AuditFields;
import com.banking.common.audit.AuditEntityListener;
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
import jakarta.persistence.UniqueConstraint;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Objects;

@Entity
@Table(name = "user_scopes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "scope_type", "scope_id", "role"})
})
@EntityListeners({AuditingEntityListener.class, AuditEntityListener.class})
public class UserScope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false)
    private ScopeType scopeType;

    @Column(name = "scope_id")
    private Long scopeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Embedded
    private AuditFields auditFields = new AuditFields("system");

    public UserScope() {
    }

    public UserScope(Long userId, ScopeType scopeType, Long scopeId, Role role) {
        this.userId = userId;
        this.scopeType = scopeType;
        this.scopeId = scopeId;
        this.role = role;
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

    public ScopeType getScopeType() {
        return scopeType;
    }

    public void setScopeType(ScopeType scopeType) {
        this.scopeType = scopeType;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
        UserScope userScope = (UserScope) o;
        return Objects.equals(id, userScope.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}