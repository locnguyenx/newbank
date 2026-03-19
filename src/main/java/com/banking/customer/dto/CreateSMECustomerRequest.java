package com.banking.customer.dto;

import com.banking.customer.domain.enums.PhoneType;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CreateSMECustomerRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    private String registrationNumber;

    private String industry;

    private String businessType;

    private String annualTurnoverAmount;

    private String annualTurnoverCurrency;

    private Integer yearsInOperation;

    private List<CreateCorporateCustomerRequest.AddressDto> addresses;

    private List<CreateCorporateCustomerRequest.PhoneDto> phones;

    private List<String> emails;

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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAnnualTurnoverAmount() {
        return annualTurnoverAmount;
    }

    public void setAnnualTurnoverAmount(String annualTurnoverAmount) {
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

    public List<CreateCorporateCustomerRequest.AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<CreateCorporateCustomerRequest.AddressDto> addresses) {
        this.addresses = addresses;
    }

    public List<CreateCorporateCustomerRequest.PhoneDto> getPhones() {
        return phones;
    }

    public void setPhones(List<CreateCorporateCustomerRequest.PhoneDto> phones) {
        this.phones = phones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
