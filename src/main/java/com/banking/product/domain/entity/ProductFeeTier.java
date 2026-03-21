package com.banking.product.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_fee_tiers")
@Deprecated
public class ProductFeeTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_entry_id", nullable = false)
    private ProductFeeEntry feeEntry;

    @Column(name = "tier_from", nullable = false)
    private Long tierFrom;

    @Column(name = "tier_to")
    private Long tierTo;

    @Column(precision = 19, scale = 6)
    private BigDecimal rate;

    protected ProductFeeTier() {
    }

    public ProductFeeTier(ProductFeeEntry feeEntry, Long tierFrom, Long tierTo, BigDecimal rate) {
        this.feeEntry = feeEntry;
        this.tierFrom = tierFrom;
        this.tierTo = tierTo;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public ProductFeeEntry getFeeEntry() {
        return feeEntry;
    }

    public void setFeeEntry(ProductFeeEntry feeEntry) {
        this.feeEntry = feeEntry;
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
