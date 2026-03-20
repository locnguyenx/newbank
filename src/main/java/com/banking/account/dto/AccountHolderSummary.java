package com.banking.account.dto;

import com.banking.account.domain.enums.AccountHolderRole;
import com.banking.account.domain.enums.AccountHolderStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AccountHolderSummary {

    private Long id;
    private String customerName;
    private AccountHolderRole role;
    private AccountHolderStatus status;
    private LocalDate effectiveFrom;

    public AccountHolderSummary() {
    }

    public AccountHolderSummary(Long id, String customerName, AccountHolderRole role,
                               AccountHolderStatus status, LocalDate effectiveFrom) {
        this.id = id;
        this.customerName = customerName;
        this.role = role;
        this.status = status;
        this.effectiveFrom = effectiveFrom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public AccountHolderRole getRole() {
        return role;
    }

    public void setRole(AccountHolderRole role) {
        this.role = role;
    }

    public AccountHolderStatus getStatus() {
        return status;
    }

    public void setStatus(AccountHolderStatus status) {
        this.status = status;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
