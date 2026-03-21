package com.banking.masterdata.domain.entity;

import com.banking.masterdata.domain.embeddable.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "document_types")
public class DocumentType {

    @Id
    @Column(name = "code", length = 30)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean isActive = true;

    @Embedded
    private AuditFields audit;

    protected DocumentType() {
    }

    public DocumentType(String code, String name, String category) {
        this.code = code;
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
