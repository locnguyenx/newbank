package com.banking.masterdata.domain.entity;

import com.banking.masterdata.domain.embeddable.AuditFields;
import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @Column(length = 3)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 5)
    private String symbol;

    @Column(nullable = false)
    private int decimalPlaces;

    @Column(nullable = false)
    private boolean isActive = true;

    @Embedded
    private AuditFields audit;

    protected Currency() {
    }

    public Currency(String code, String name, String symbol, int decimalPlaces) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.decimalPlaces = decimalPlaces;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
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
