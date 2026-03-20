package com.banking.account.dto;

import com.banking.account.domain.enums.AccountHolderRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class AccountHolderRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Role is required")
    private AccountHolderRole role;

    public AccountHolderRequest() {
    }

    public AccountHolderRequest(Long customerId, AccountHolderRole role) {
        this.customerId = customerId;
        this.role = role;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public AccountHolderRole getRole() {
        return role;
    }

    public void setRole(AccountHolderRole role) {
        this.role = role;
    }
}
