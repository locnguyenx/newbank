package com.banking.customer.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class UpdateCustomerRequest {

    @NotBlank(message = "Name is required")
    private String name;
    private String taxId;
    private String registrationNumber;
    private String industry;
    private String email;
    private String phoneNumber;

    // Corporate-only fields
    private Long annualRevenueAmount;
    private String annualRevenueCurrency;
    private Integer employeeCount;
    private String website;

    // SME-only fields
    private String businessType;
    private Long annualTurnoverAmount;
    private String annualTurnoverCurrency;
    private Integer yearsInOperation;

    // Individual-only fields
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String nationality;
    private Long employerId;
    private String employmentStatus;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getAnnualRevenueAmount() {
        return annualRevenueAmount;
    }

    public void setAnnualRevenueAmount(Long annualRevenueAmount) {
        this.annualRevenueAmount = annualRevenueAmount;
    }

    public String getAnnualRevenueCurrency() {
        return annualRevenueCurrency;
    }

    public void setAnnualRevenueCurrency(String annualRevenueCurrency) {
        this.annualRevenueCurrency = annualRevenueCurrency;
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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getAnnualTurnoverAmount() {
        return annualTurnoverAmount;
    }

    public void setAnnualTurnoverAmount(Long annualTurnoverAmount) {
        this.annualTurnoverAmount = annualTurnoverAmount;
    }

    public String getAnnualTurnoverCurrency() {
        return annualTurnoverCurrency;
    }

    public void setAnnualTurnoverCurrency(String annualTurnoverCurrency) {
        this.annualTurnoverCurrency = annualTurnoverCurrency;
    }

    public Integer getYearsInOperation() {
        return yearsInOperation;
    }

    public void setYearsInOperation(Integer yearsInOperation) {
        this.yearsInOperation = yearsInOperation;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
}
