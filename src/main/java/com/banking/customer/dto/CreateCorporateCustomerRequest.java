package com.banking.customer.dto;

import com.banking.customer.domain.enums.PhoneType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CreateCorporateCustomerRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    private String registrationNumber;

    private String industry;

    private String annualRevenueAmount;

    private String annualRevenueCurrency;

    private Integer employeeCount;

    private String website;

    private List<AddressDto> addresses;

    private List<PhoneDto> phones;

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

    public String getAnnualRevenueAmount() {
        return annualRevenueAmount;
    }

    public void setAnnualRevenueAmount(String annualRevenueAmount) {
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

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }

    public List<PhoneDto> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDto> phones) {
        this.phones = phones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public static class AddressDto {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class PhoneDto {
        private String countryCode;
        private String number;
        private PhoneType type;

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public PhoneType getType() {
            return type;
        }

        public void setType(PhoneType type) {
            this.type = type;
        }
    }
}
