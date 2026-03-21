package com.banking.charges.domain.entity;

import com.banking.charges.domain.embeddable.AuditFields;
import com.banking.charges.domain.enums.WaiverScope;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "fee_waivers")
public class FeeWaiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaiverScope scope;

    @Column(name = "reference_id", nullable = false, length = 50)
    private String referenceId;

    @Column(name = "waiver_percentage", nullable = false)
    private Integer waiverPercentage;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Embedded
    private AuditFields audit;

    protected FeeWaiver() {
    }

    public FeeWaiver(ChargeDefinition chargeDefinition, WaiverScope scope, String referenceId,
                     Integer waiverPercentage, LocalDate validFrom, LocalDate validTo) {
        this.chargeDefinition = chargeDefinition;
        this.scope = scope;
        this.referenceId = referenceId;
        this.waiverPercentage = waiverPercentage;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChargeDefinition getChargeDefinition() {
        return chargeDefinition;
    }

    public void setChargeDefinition(ChargeDefinition chargeDefinition) {
        this.chargeDefinition = chargeDefinition;
    }

    public WaiverScope getScope() {
        return scope;
    }

    public void setScope(WaiverScope scope) {
        this.scope = scope;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getWaiverPercentage() {
        return waiverPercentage;
    }

    public void setWaiverPercentage(Integer waiverPercentage) {
        this.waiverPercentage = waiverPercentage;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }

    public boolean isActiveOn(LocalDate date) {
        return !date.isBefore(validFrom) && (validTo == null || !date.isAfter(validTo));
    }
}
