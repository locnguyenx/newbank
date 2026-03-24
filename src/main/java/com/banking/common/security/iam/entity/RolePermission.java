package com.banking.common.security.iam.entity;

import com.banking.common.audit.AuditEntityListener;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "role_permissions")
@EntityListeners({org.springframework.data.jpa.domain.support.AuditingEntityListener.class, AuditEntityListener.class})
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(nullable = false)
    private String resource;

    @Column(nullable = false)
    private String action;

    public RolePermission() {
    }

    public RolePermission(Long roleId, String resource, String action) {
        this.roleId = roleId;
        this.resource = resource;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}