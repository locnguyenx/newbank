package com.banking.account.domain.entity;

import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_account")
@DiscriminatorValue(value = "LOAN")
public class LoanAccount extends Account {

    @Column(name = "loan_amount", precision = 19, scale = 2)
    private BigDecimal loanAmount;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "term")
    private Integer term;

    @Column(name = "outstanding_balance", precision = 19, scale = 2)
    private BigDecimal outstandingBalance;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;

    protected LoanAccount() {
        super();
    }

    public LoanAccount(String accountNumber, Long customerId, Long productId,
                       String currency, BigDecimal loanAmount, BigDecimal interestRate, Integer term) {
        super(accountNumber, AccountType.LOAN, AccountStatus.ACTIVE, currency,
              BigDecimal.ZERO, customerId, productId);
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.term = term;
        this.outstandingBalance = loanAmount;
        this.nextPaymentDate = getOpenedAt().toLocalDate().plusMonths(1);
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }
}
