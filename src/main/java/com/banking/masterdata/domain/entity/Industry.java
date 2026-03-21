package com.banking.masterdata.domain.entity;

import com.banking.masterdata.domain.embeddable.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "industries")
public class Industry {

    @Id
    @Column(length = 10)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 10)
    private String parentCode;

    @Column(nullable = false)
    private boolean isActive = true;

    @Embedded
    private AuditFields audit;

    protected Industry() {
    }

    public Industry(String code, String name, String parentCode) {
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
