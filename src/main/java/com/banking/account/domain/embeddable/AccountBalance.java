package com.banking.account.domain.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class AccountBalance {

    private BigDecimal availableBalance;
    private BigDecimal ledgerBalance;
    private BigDecimal holdAmount;

    @Column(length = 3)
    private String currency;

    public AccountBalance() {
    }

    public AccountBalance(BigDecimal availableBalance, BigDecimal ledgerBalance, BigDecimal holdAmount, String currency) {
        this.availableBalance = availableBalance;
        this.ledgerBalance = ledgerBalance;
        this.holdAmount = holdAmount;
        this.currency = currency;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(BigDecimal ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBalance that = (AccountBalance) o;
        return java.util.Objects.equals(availableBalance, that.availableBalance) &&
               java.util.Objects.equals(ledgerBalance, that.ledgerBalance) &&
               java.util.Objects.equals(holdAmount, that.holdAmount) &&
               java.util.Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(availableBalance, ledgerBalance, holdAmount, currency);
    }
}
