package com.banking.account.dto;

import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.domain.enums.Currency;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {

    private Long id;

    @NotNull(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Account type is required")
    private AccountType type;

    @NotNull(message = "Account status is required")
    private AccountStatus status;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @NotNull(message = "Balance is required")
    private BigDecimal balance;

    private Long productId;

    private Long productVersionId;

    private String productName;

    private Long customerId;

    private LocalDateTime openedAt;

    private LocalDateTime closedAt;

    // Constructors
    public AccountResponse() {
    }

    public AccountResponse(Long id, String accountNumber, AccountType type, AccountStatus status,
                          Currency currency, BigDecimal balance, Long productId,
                          Long productVersionId, String productName,
                          Long customerId, LocalDateTime openedAt, LocalDateTime closedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.status = status;
        this.currency = currency;
        this.balance = balance;
        this.productId = productId;
        this.productVersionId = productVersionId;
        this.productName = productName;
        this.customerId = customerId;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductVersionId() {
        return productVersionId;
    }

    public void setProductVersionId(Long productVersionId) {
        this.productVersionId = productVersionId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
}
