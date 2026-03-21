package com.banking.masterdata.domain.entity;

import com.banking.masterdata.domain.embeddable.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "channels")
public class Channel {

    @Id
    @Column(length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isActive = true;

    @Embedded
    private AuditFields audit;

    protected Channel() {
    }

    public Channel(String code, String name) {
        this.code = code;
        this.name = name;
        this.isActive = true;
        this.audit = new AuditFields();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
