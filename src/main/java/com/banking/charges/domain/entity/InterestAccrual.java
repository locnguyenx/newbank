package com.banking.charges.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "interest_accruals", uniqueConstraints = @UniqueConstraint(columnNames = {"interest_rate_id", "account_number", "accrual_date"}))
public class InterestAccrual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_rate_id", nullable = false)
    private InterestRate interestRate;

    @Column(name = "account_number", nullable = false, length = 30)
    private String accountNumber;

    @Column(name = "accrual_date", nullable = false)
    private LocalDate accrualDate;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal amount;

    @Column(nullable = false)
    private Boolean applied = false;

    protected InterestAccrual() {
    }

    public InterestAccrual(InterestRate interestRate, String accountNumber, LocalDate accrualDate, BigDecimal balance, BigDecimal amount) {
        this.interestRate = interestRate;
        this.accountNumber = accountNumber;
        this.accrualDate = accrualDate;
        this.balance = balance;
        this.amount = amount;
        this.applied = false;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getAccrualDate() {
        return accrualDate;
    }

    public void setAccrualDate(LocalDate accrualDate) {
        this.accrualDate = accrualDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getApplied() {
        return applied;
    }

    public void setApplied(Boolean applied) {
        this.applied = applied;
    }
}
