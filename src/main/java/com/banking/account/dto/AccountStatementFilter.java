package com.banking.account.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AccountStatementFilter {

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    private String transactionType;

    public AccountStatementFilter() {
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
