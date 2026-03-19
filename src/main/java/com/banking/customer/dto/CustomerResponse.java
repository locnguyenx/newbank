package com.banking.customer.dto;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.SMECustomer;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.CustomerType;
import com.banking.customer.domain.enums.EmploymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerResponse {

    private Long id;
    private String customerNumber;
    private CustomerType type;
    private CustomerStatus status;
    private String name;
    private String taxId;

    private String registrationNumber;
    private String industry;
    private BigDecimal annualRevenueAmount;
    private String annualRevenueCurrency;
    private Integer employeeCount;
    private String website;

    private String businessType;
    private BigDecimal annualTurnoverAmount;
    private String annualTurnoverCurrency;
    private Integer yearsInOperation;

    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String nationality;
    private Long employerId;
    private EmploymentStatus employmentStatus;

    private List<AddressDto> addresses;
    private List<PhoneDto> phones;
    private List<String> emails;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    public static CustomerResponse fromEntity(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setCustomerNumber(customer.getCustomerNumber());
        response.setType(customer.getType());
        response.setStatus(customer.getStatus());
        response.setName(customer.getName());
        response.setTaxId(customer.getTaxId());
        response.setAddresses(customer.getAddresses().stream()
                .map(a -> {
                    AddressDto dto = new AddressDto();
                    dto.setStreet(a.getStreet());
                    dto.setCity(a.getCity());
                    dto.setState(a.getState());
                    dto.setPostalCode(a.getPostalCode());
                    dto.setCountry(a.getCountry());
                    return dto;
                })
                .collect(Collectors.toList()));
        response.setPhones(customer.getPhones().stream()
                .map(p -> {
                    PhoneDto dto = new PhoneDto();
                    dto.setCountryCode(p.getCountryCode());
                    dto.setNumber(p.getNumber());
                    dto.setType(p.getType());
                    return dto;
                })
                .collect(Collectors.toList()));
        response.setEmails(customer.getEmails());
        if (customer.getAuditFields() != null) {
            response.setCreatedAt(customer.getAuditFields().getCreatedAt());
            response.setCreatedBy(customer.getAuditFields().getCreatedBy());
            response.setUpdatedAt(customer.getAuditFields().getUpdatedAt());
            response.setUpdatedBy(customer.getAuditFields().getUpdatedBy());
        }

        if (customer instanceof CorporateCustomer corporate) {
            response.setRegistrationNumber(corporate.getRegistrationNumber());
            response.setIndustry(corporate.getIndustry());
            if (corporate.getAnnualRevenue() != null) {
                response.setAnnualRevenueAmount(corporate.getAnnualRevenue().getAmount());
                response.setAnnualRevenueCurrency(corporate.getAnnualRevenue().getCurrency());
            }
            response.setEmployeeCount(corporate.getEmployeeCount());
            response.setWebsite(corporate.getWebsite());
        }

        if (customer instanceof SMECustomer sme) {
            response.setRegistrationNumber(sme.getRegistrationNumber());
            response.setIndustry(sme.getIndustry());
            response.setBusinessType(sme.getBusinessType());
            if (sme.getAnnualTurnover() != null) {
                response.setAnnualTurnoverAmount(sme.getAnnualTurnover().getAmount());
                response.setAnnualTurnoverCurrency(sme.getAnnualTurnover().getCurrency());
            }
            response.setYearsInOperation(sme.getYearsInOperation());
        }

        if (customer instanceof IndividualCustomer individual) {
            response.setDateOfBirth(individual.getDateOfBirth());
            response.setPlaceOfBirth(individual.getPlaceOfBirth());
            response.setNationality(individual.getNationality());
            response.setEmployerId(individual.getEmployerId());
            response.setEmploymentStatus(individual.getEmploymentStatus());
        }

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

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

    public BigDecimal getAnnualRevenueAmount() {
        return annualRevenueAmount;
    }

    public void setAnnualRevenueAmount(BigDecimal annualRevenueAmount) {
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

    public BigDecimal getAnnualTurnoverAmount() {
        return annualTurnoverAmount;
    }

    public void setAnnualTurnoverAmount(BigDecimal annualTurnoverAmount) {
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

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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
        private com.banking.customer.domain.enums.PhoneType type;

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

        public com.banking.customer.domain.enums.PhoneType getType() {
            return type;
        }

        public void setType(com.banking.customer.domain.enums.PhoneType type) {
            this.type = type;
        }
    }
}
