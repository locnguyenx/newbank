package com.banking.account.domain.entity;

import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.domain.enums.Currency;
import com.banking.customer.domain.entity.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savings_account")
@DiscriminatorValue(value = "SAVINGS")
public class SavingsAccount extends Account {

    @Column(name = "minimum_balance", precision = 19, scale = 2)
    private BigDecimal minimumBalance;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "last_interest_posted")
    private LocalDate lastInterestPosted;

    protected SavingsAccount() {
        super();
    }

    public SavingsAccount(String accountNumber, Customer customer, Long productId,
                          Currency currency, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(accountNumber, AccountType.SAVINGS, AccountStatus.ACTIVE, currency,
              BigDecimal.ZERO, customer, productId);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getLastInterestPosted() {
        return lastInterestPosted;
    }

    public void setLastInterestPosted(LocalDate lastInterestPosted) {
        this.lastInterestPosted = lastInterestPosted;
    }
}
