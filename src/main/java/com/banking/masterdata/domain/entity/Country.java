package com.banking.masterdata.domain.entity;

import com.banking.masterdata.domain.embeddable.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Country {

    @Id
    @Column(name = "iso_code", length = 2)
    private String isoCode;

    @Column(nullable = false)
    private String name;

    @Column(length = 20)
    private String region;

    @Column(nullable = false)
    private boolean isActive = true;

    @Embedded
    private AuditFields audit;

    protected Country() {
    }

    public Country(String isoCode, String name, String region) {
        this.isoCode = isoCode;
        this.name = name;
        this.region = region;
        this.isActive = true;
        this.audit = new AuditFields();
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
