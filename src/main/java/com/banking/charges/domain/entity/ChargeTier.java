package com.banking.charges.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "charge_tiers")
public class ChargeTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_rule_id", nullable = false)
    private ChargeRule chargeRule;

    @Column(name = "tier_from", nullable = false)
    private Long tierFrom;

    @Column(name = "tier_to")
    private Long tierTo;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    protected ChargeTier() {
    }

    public ChargeTier(ChargeRule chargeRule, Long tierFrom, Long tierTo, BigDecimal rate) {
        this.chargeRule = chargeRule;
        this.tierFrom = tierFrom;
        this.tierTo = tierTo;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChargeRule getChargeRule() {
        return chargeRule;
    }

    public void setChargeRule(ChargeRule chargeRule) {
        this.chargeRule = chargeRule;
    }

    public Long getTierFrom() {
        return tierFrom;
    }

    public void setTierFrom(Long tierFrom) {
        this.tierFrom = tierFrom;
    }

    public Long getTierTo() {
        return tierTo;
    }

    public void setTierTo(Long tierTo) {
        this.tierTo = tierTo;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
