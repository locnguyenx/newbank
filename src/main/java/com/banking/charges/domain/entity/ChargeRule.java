package com.banking.charges.domain.entity;

import com.banking.charges.domain.embeddable.AuditFields;
import com.banking.charges.domain.enums.CalculationMethod;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "charge_rules")
public class ChargeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_definition_id", nullable = false)
    private ChargeDefinition chargeDefinition;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_method", nullable = false)
    private CalculationMethod method;

    @Column(name = "flat_amount", precision = 19, scale = 4)
    private BigDecimal flatAmount;

    @Column(name = "percentage_rate", precision = 10, scale = 6)
    private BigDecimal percentageRate;

    @Column(name = "min_amount", precision = 19, scale = 4)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = 19, scale = 4)
    private BigDecimal maxAmount;

    @OneToMany(mappedBy = "chargeRule", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("tierFrom ASC")
    private List<ChargeTier> tiers = new ArrayList<>();

    @Embedded
    private AuditFields audit;

    protected ChargeRule() {
    }

    public ChargeRule(ChargeDefinition chargeDefinition, CalculationMethod method, BigDecimal flatAmount,
                      BigDecimal percentageRate, BigDecimal minAmount, BigDecimal maxAmount) {
        this.chargeDefinition = chargeDefinition;
        this.method = method;
        this.flatAmount = flatAmount;
        this.percentageRate = percentageRate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
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

    public CalculationMethod getMethod() {
        return method;
    }

    public void setMethod(CalculationMethod method) {
        this.method = method;
    }

    public BigDecimal getFlatAmount() {
        return flatAmount;
    }

    public void setFlatAmount(BigDecimal flatAmount) {
        this.flatAmount = flatAmount;
    }

    public BigDecimal getPercentageRate() {
        return percentageRate;
    }

    public void setPercentageRate(BigDecimal percentageRate) {
        this.percentageRate = percentageRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<ChargeTier> getTiers() {
        return tiers;
    }

    public void setTiers(List<ChargeTier> tiers) {
        this.tiers = tiers;
    }

    public void addTier(ChargeTier tier) {
        tiers.add(tier);
        tier.setChargeRule(this);
    }

    public void removeTier(ChargeTier tier) {
        tiers.remove(tier);
        tier.setChargeRule(null);
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
