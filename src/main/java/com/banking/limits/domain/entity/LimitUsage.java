package com.banking.limits.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "limit_usage",
    uniqueConstraints = @UniqueConstraint(columnNames = {"limit_definition_id", "account_number", "period_start"})
)
public class LimitUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_definition_id", nullable = false)
    private LimitDefinition limitDefinition;

    @Column(name = "account_number", nullable = false, length = 30)
    private String accountNumber;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "cumulative_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal cumulativeAmount = BigDecimal.ZERO;

    protected LimitUsage() {
    }

    public LimitUsage(LimitDefinition limitDefinition, String accountNumber,
                      LocalDate periodStart, LocalDate periodEnd) {
        this.limitDefinition = limitDefinition;
        this.accountNumber = accountNumber;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.cumulativeAmount = BigDecimal.ZERO;
    }

    public void addAmount(BigDecimal amount) {
        this.cumulativeAmount = this.cumulativeAmount.add(amount);
    }

    public Long getId() {
        return id;
    }

    public LimitDefinition getLimitDefinition() {
        return limitDefinition;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public BigDecimal getCumulativeAmount() {
        return cumulativeAmount;
    }

    public void setCumulativeAmount(BigDecimal cumulativeAmount) {
        this.cumulativeAmount = cumulativeAmount;
    }
}
