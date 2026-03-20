package com.banking.account.domain.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.math.BigDecimal;
import com.banking.account.domain.enums.Currency;

@Embeddable
public class AccountBalance {

    private BigDecimal availableBalance;
    private BigDecimal ledgerBalance;
    private BigDecimal holdAmount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public AccountBalance() {
    }

    public AccountBalance(BigDecimal availableBalance, BigDecimal ledgerBalance, BigDecimal holdAmount, Currency currency) {
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
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
               currency == that.currency;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(availableBalance, ledgerBalance, holdAmount, currency);
    }
}
