package com.banking.account.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class AccountStatementResponse {
    private String accountNumber;
    private String currency;
    private Instant fromDate;
    private Instant toDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private List<StatementEntry> entries;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public List<StatementEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<StatementEntry> entries) {
        this.entries = entries;
    }
}
