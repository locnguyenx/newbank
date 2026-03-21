package com.banking.customer.domain.entity;

import com.banking.customer.domain.embeddable.Money;
import com.banking.customer.domain.enums.CustomerStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "corporate_customers")
@DiscriminatorValue("CORPORATE")
public class CorporateCustomer extends Customer {

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "industry")
    private String industry;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "annual_revenue_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "annual_revenue_currency"))
    private Money annualRevenue;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "website")
    private String website;

    protected CorporateCustomer() {
    }

    public CorporateCustomer(String customerNumber, String name, CustomerStatus status) {
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

    public Money getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(Money annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
