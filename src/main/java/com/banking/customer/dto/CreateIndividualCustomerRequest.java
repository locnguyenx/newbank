package com.banking.customer.dto;

import com.banking.customer.domain.enums.EmploymentStatus;
import com.banking.customer.domain.enums.PhoneType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

public class CreateIndividualCustomerRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String nationality;

    private Long employerId;

    private EmploymentStatus employmentStatus;

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
