package com.banking.account.domain.entity;

import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import com.banking.customer.domain.entity.Customer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fixed_deposit_account")
@DiscriminatorValue(value = "FIXED_DEPOSIT")
public class FixedDepositAccount extends Account {

    @Column(name = "deposit_term")
    private Integer depositTerm;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "maturity_amount", precision = 19, scale = 2)
    private BigDecimal maturityAmount;

    protected FixedDepositAccount() {
        super();
    }

    public FixedDepositAccount(String accountNumber, Customer customer, Long productId,
                               String currency, Integer depositTerm, BigDecimal maturityAmount) {
        super(accountNumber, AccountType.FIXED_DEPOSIT, AccountStatus.ACTIVE, currency,
              BigDecimal.ZERO, customer, productId);
        this.depositTerm = depositTerm;
        this.maturityDate = getOpenedAt().toLocalDate().plusMonths(depositTerm);
        this.maturityAmount = maturityAmount;
    }

    public Integer getDepositTerm() {
        return depositTerm;
    }

    public void setDepositTerm(Integer depositTerm) {
        this.depositTerm = depositTerm;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public BigDecimal getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(BigDecimal maturityAmount) {
        this.maturityAmount = maturityAmount;
    }
}
