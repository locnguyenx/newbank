package com.banking.customer.domain.entity;

import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.EmploymentStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("INDIVIDUAL")
public class IndividualCustomer extends Customer {

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "employer_id")
    private Long employerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    protected IndividualCustomer() {
    }

    public IndividualCustomer(String customerNumber, String name, CustomerStatus status) {
        super(customerNumber, name, status);
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
}
