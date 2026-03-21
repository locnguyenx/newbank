package com.banking.charges.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "interest_tiers")
public class InterestTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_rate_id", nullable = false)
    private InterestRate interestRate;

    @Column(name = "balance_from", nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceFrom;

    @Column(name = "balance_to", precision = 19, scale = 4)
    private BigDecimal balanceTo;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal rate;

    protected InterestTier() {
    }

    public InterestTier(InterestRate interestRate, BigDecimal balanceFrom, BigDecimal balanceTo, BigDecimal rate) {
        this.interestRate = interestRate;
        this.balanceFrom = balanceFrom;
        this.balanceTo = balanceTo;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterestRate getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(InterestRate interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getBalanceFrom() {
        return balanceFrom;
    }

    public void setBalanceFrom(BigDecimal balanceFrom) {
        this.balanceFrom = balanceFrom;
    }

    public BigDecimal getBalanceTo() {
        return balanceTo;
    }

    public void setBalanceTo(BigDecimal balanceTo) {
        this.balanceTo = balanceTo;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
