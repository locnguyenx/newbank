package com.banking.charges.dto.response;

import com.banking.charges.domain.entity.InterestAccrual;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InterestAccrualResponse {

    private Long id;
    private Long interestRateId;
    private String accountNumber;
    private LocalDate accrualDate;
    private BigDecimal balance;
    private BigDecimal amount;
    private Boolean applied;

    public static InterestAccrualResponse fromEntity(InterestAccrual entity) {
        InterestAccrualResponse response = new InterestAccrualResponse();
        response.id = entity.getId();
        response.interestRateId = entity.getInterestRate().getId();
        response.accountNumber = entity.getAccountNumber();
        response.accrualDate = entity.getAccrualDate();
        response.balance = entity.getBalance();
        response.amount = entity.getAmount();
        response.applied = entity.getApplied();
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInterestRateId() {
        return interestRateId;
    }

    public void setInterestRateId(Long interestRateId) {
        this.interestRateId = interestRateId;
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
