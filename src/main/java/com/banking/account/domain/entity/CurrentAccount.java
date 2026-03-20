package com.banking.account.domain.entity;

import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.domain.enums.Currency;
import com.banking.customer.domain.entity.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "current_account")
@DiscriminatorValue(value = "CURRENT")
public class CurrentAccount extends Account {

    @Column(name = "overdraft_limit", precision = 19, scale = 2)
    private BigDecimal overdraftLimit;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    protected CurrentAccount() {
        super();
    }

    public CurrentAccount(String accountNumber, Customer customer, Long productId,
                          Currency currency, BigDecimal overdraftLimit, BigDecimal interestRate) {
        super(accountNumber, AccountType.CURRENT, AccountStatus.ACTIVE, currency,
              BigDecimal.ZERO, customer, productId);
        this.overdraftLimit = overdraftLimit;
        this.interestRate = interestRate;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
