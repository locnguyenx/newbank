package com.banking.customer.domain.entity;

import com.banking.customer.domain.embeddable.AuditFields;
import com.banking.customer.domain.enums.EmploymentStatus;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;

@Entity
@Table(name = "employment_relationships")
@EntityListeners(AuditingEntityListener.class)
public class EmploymentRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private IndividualCustomer employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Customer employer;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmploymentStatus status;

    @Embedded
    private AuditFields auditFields;

    protected EmploymentRelationship() {
    }

    public EmploymentRelationship(IndividualCustomer employee, Customer employer, String employeeNumber,
                                   String department, String position, LocalDate startDate, EmploymentStatus status) {
        this.employee = employee;
        this.employer = employer;
        this.employeeNumber = employeeNumber;
        this.department = department;
        this.position = position;
        this.startDate = startDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public IndividualCustomer getEmployee() {
        return employee;
    }

    public void setEmployee(IndividualCustomer employee) {
        this.employee = employee;
    }

    public Customer getEmployer() {
        return employer;
    }

    public void setEmployer(Customer employer) {
        this.employer = employer;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EmploymentStatus getStatus() {
        return status;
    }

    public void setStatus(EmploymentStatus status) {
        this.status = status;
    }

    public AuditFields getAuditFields() {
        return auditFields;
    }
}