package com.banking.account.dto;

import java.math.BigDecimal;

public class CustomerAccountSummary {

    private Long customerId;
    private BigDecimal totalBalance;
    private int accountCount;
    private String currency;

    public CustomerAccountSummary() {
    }

    public CustomerAccountSummary(Long customerId, BigDecimal totalBalance, int accountCount, String currency) {
        this.customerId = customerId;
        this.totalBalance = totalBalance;
        this.accountCount = accountCount;
        this.currency = currency;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void setAccountCount(int accountCount) {
        this.accountCount = accountCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
