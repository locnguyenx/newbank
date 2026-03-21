package com.banking.customer.domain.entity;

import com.banking.customer.domain.embeddable.Money;
import com.banking.customer.domain.enums.CustomerStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "sme_customers")
@DiscriminatorValue("SME")
public class SMECustomer extends Customer {

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "industry")
    private String industry;

    @Column(name = "business_type")
    private String businessType;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "annual_turnover_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "annual_turnover_currency"))
    private Money annualTurnover;

    @Column(name = "years_in_operation")
    private Integer yearsInOperation;

    protected SMECustomer() {
    }

    public SMECustomer(String customerNumber, String name, CustomerStatus status) {
        super(customerNumber, name, status);
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Money getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Money annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public Integer getYearsInOperation() {
        return yearsInOperation;
    }

    public void setYearsInOperation(Integer yearsInOperation) {
        this.yearsInOperation = yearsInOperation;
    }
}
