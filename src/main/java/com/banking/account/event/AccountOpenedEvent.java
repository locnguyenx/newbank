package com.banking.account.event;

import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * Event published when an account is successfully opened.
 * <p>
 * This event is used by other modules (e.g., Limits module) to perform
 * side effects asynchronously, such as assigning default limits to the new account.
 * </p>
 * <p>
 * By using events, the Account module maintains loose coupling with other modules.
 * It only publishes the event; it does not know or care which modules consume it.
 * </p>
 */
public class AccountOpenedEvent extends ApplicationEvent {

    private final Long accountId;
    private final String accountNumber;
    private final Long customerId;
    private final String productCode;
    private final Long productId;
    private final Long productVersionId;
    private final String productName;
    private final String currency;
    private final BigDecimal initialBalance;
    private final String createdBy;

    /**
     * Constructs a new AccountOpenedEvent.
     *
     * @param source The source object (usually the AccountService)
     * @param accountId The generated account ID
     * @param accountNumber The account number
     * @param customerId The customer ID
     * @param productCode The product code (e.g., "CURRENT", "SAVINGS")
     * @param productId The product ID
     * @param productVersionId The product version ID
     * @param productName The product name
     * @param currency The currency code (e.g., "USD")
     * @param initialBalance The initial deposit amount
     * @param createdBy The user who created the account
     */
    public AccountOpenedEvent(Object source,
                              Long accountId,
                              String accountNumber,
                              Long customerId,
                              String productCode,
                              Long productId,
                              Long productVersionId,
                              String productName,
                              String currency,
                              BigDecimal initialBalance,
                              String createdBy) {
        super(source);
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.productCode = productCode;
        this.productId = productId;
        this.productVersionId = productVersionId;
        this.productName = productName;
        this.currency = currency;
        this.initialBalance = initialBalance;
        this.createdBy = createdBy;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getProductVersionId() {
        return productVersionId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}