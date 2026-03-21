package com.banking.charges.domain.entity;

import com.banking.charges.domain.embeddable.AuditFields;
import com.banking.charges.domain.enums.InterestSchedule;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interest_rates")
public class InterestRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;

    @Column(name = "fixed_rate", precision = 10, scale = 6)
    private BigDecimal fixedRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "accrual_schedule", nullable = false)
    private InterestSchedule accrualSchedule = InterestSchedule.DAILY;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_schedule", nullable = false)
    private InterestSchedule applicationSchedule = InterestSchedule.MONTHLY;

    @OneToMany(mappedBy = "interestRate", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("balanceFrom ASC")
    private List<InterestTier> tiers = new ArrayList<>();

    @Embedded
    private AuditFields audit;

    protected InterestRate() {
    }

    public InterestRate(ChargeDefinition chargeDefinition, String productCode, BigDecimal fixedRate,
                       InterestSchedule accrualSchedule, InterestSchedule applicationSchedule) {
        this.chargeDefinition = chargeDefinition;
        this.productCode = productCode;
        this.fixedRate = fixedRate;
        this.accrualSchedule = accrualSchedule;
        this.applicationSchedule = applicationSchedule;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public InterestSchedule getAccrualSchedule() {
        return accrualSchedule;
    }

    public void setAccrualSchedule(InterestSchedule accrualSchedule) {
        this.accrualSchedule = accrualSchedule;
    }

    public InterestSchedule getApplicationSchedule() {
        return applicationSchedule;
    }

    public void setApplicationSchedule(InterestSchedule applicationSchedule) {
        this.applicationSchedule = applicationSchedule;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }

    public List<InterestTier> getTiers() {
        return tiers;
    }

    public void setTiers(List<InterestTier> tiers) {
        this.tiers = tiers;
    }

    public void addTier(InterestTier tier) {
        tiers.add(tier);
        tier.setInterestRate(this);
    }
}
