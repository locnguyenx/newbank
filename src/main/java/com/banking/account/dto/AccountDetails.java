package com.banking.account.dto;

import com.banking.account.domain.embeddable.AccountBalance;
import jakarta.validation.constraints.NotNull;

public class AccountDetails extends AccountResponse {

    @NotNull
    private AccountBalance balance;

    private java.util.List<AccountHolderSummary> holders;

    public AccountDetails() {
        super();
    }

    public AccountBalance getAccountBalance() {
        return balance;
    }

    public void setAccountBalance(AccountBalance balance) {
        this.balance = balance;
    }

    public java.util.List<AccountHolderSummary> getHolders() {
        return holders;
    }

    public void setHolders(java.util.List<AccountHolderSummary> holders) {
        this.holders = holders;
    }
}
