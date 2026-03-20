package com.banking.product.domain.entity;

import com.banking.product.domain.embeddable.AuditFields;
import com.banking.product.domain.enums.FeeCalculationMethod;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_fee_entries")
public class ProductFeeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Column(name = "fee_type", nullable = false, length = 50)
    private String feeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_method", nullable = false, length = 30)
    private FeeCalculationMethod calculationMethod;

    @Column(precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(precision = 10, scale = 6)
    private BigDecimal rate;

    @Column(length = 3)
    private String currency;

    @Embedded
    private AuditFields audit;

    @OneToMany(mappedBy = "feeEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeeTier> tiers = new ArrayList<>();

    protected ProductFeeEntry() {
    }

    public ProductFeeEntry(ProductVersion productVersion, String feeType, FeeCalculationMethod calculationMethod,
                           BigDecimal amount, BigDecimal rate, String currency) {
        this.productVersion = productVersion;
        this.feeType = feeType;
        this.calculationMethod = calculationMethod;
        this.amount = amount;
        this.rate = rate;
        this.currency = currency;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public ProductVersion getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(ProductVersion productVersion) {
        this.productVersion = productVersion;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public FeeCalculationMethod getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(FeeCalculationMethod calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }

    public List<ProductFeeTier> getTiers() {
        return tiers;
    }

    public void setTiers(List<ProductFeeTier> tiers) {
        this.tiers = tiers;
    }
}
