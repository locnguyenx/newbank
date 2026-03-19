package com.banking.customer.service;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.EmploymentRelationship;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.SMECustomer;
import com.banking.customer.domain.enums.EmploymentStatus;
import com.banking.customer.dto.BulkUploadResult;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.repository.EmploymentRelationshipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class EmploymentRelationshipService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final EmploymentRelationshipRepository employmentRelationshipRepository;
    private final CustomerRepository customerRepository;

    public EmploymentRelationshipService(EmploymentRelationshipRepository employmentRelationshipRepository,
                                         CustomerRepository customerRepository) {
        this.employmentRelationshipRepository = employmentRelationshipRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public EmploymentRelationship createEmployment(IndividualCustomer employee, Customer employer,
                                                   EmploymentRelationship details) {
        validateEmployeeIsIndividual(employee);
        validateEmployerIsCorporateOrSME(employer);

        EmploymentRelationship relationship = new EmploymentRelationship(
            employee,
            employer,
            details.getEmployeeNumber(),
            details.getDepartment(),
            details.getPosition(),
            details.getStartDate(),
            EmploymentStatus.ACTIVE
        );

        return employmentRelationshipRepository.save(relationship);
    }

    @Transactional
    public EmploymentRelationship terminateEmployment(Long employmentId) {
        EmploymentRelationship employment = employmentRelationshipRepository.findById(employmentId)
            .orElseThrow(() -> new RuntimeException("Employment relationship not found: " + employmentId));

        employment.setStatus(EmploymentStatus.TERMINATED);
        employment.setEndDate(LocalDate.now());

        return employmentRelationshipRepository.save(employment);
    }

    @Transactional
    public BulkUploadResult bulkUploadEmployees(Customer employer, String csvContent) {
        BulkUploadResult result = new BulkUploadResult();
        String[] lines = csvContent.split("\\r?\\n");

        if (lines.length <= 1) {
            return result;
        }

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            try {
                processCsvLine(line, employer, result);
            } catch (Exception e) {
                result.incrementFailureCount();
                result.addError("Line " + (i + 1) + ": " + e.getMessage());
            }
        }

        return result;
    }

    private void processCsvLine(String line, Customer employer, BulkUploadResult result) {
        String[] parts = parseCsvLine(line);

        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid CSV format. Expected: employeeNumber,department,position,startDate");
        }

        String employeeNumber = trimField(parts[0]);
        String department = trimField(parts[1]);
        String position = trimField(parts[2]);
        String startDateStr = trimField(parts[3]);

        LocalDate startDate = parseDate(startDateStr);

        customerRepository.findByCustomerNumber(employeeNumber)
            .ifPresentOrElse(
                customer -> {
                    if (!(customer instanceof IndividualCustomer)) {
                        throw new IllegalArgumentException("Customer " + employeeNumber + " is not an IndividualCustomer");
                    }
                    createEmployment((IndividualCustomer) customer, employer, department, position, startDate);
                    result.incrementSuccessCount();
                },
                () -> {
                    throw new IllegalArgumentException("Customer not found: " + employeeNumber);
                }
            );
    }

    private String[] parseCsvLine(String line) {
        return line.split(",");
    }

    private String trimField(String field) {
        return field != null ? field.trim() : "";
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd, got: " + dateStr);
        }
    }

    private void validateEmployeeIsIndividual(IndividualCustomer employee) {
        if (!(employee instanceof IndividualCustomer)) {
            throw new IllegalArgumentException("Employee must be an IndividualCustomer");
        }
    }

    private void validateEmployerIsCorporateOrSME(Customer employer) {
        if (!(employer instanceof CorporateCustomer) && !(employer instanceof SMECustomer)) {
            throw new IllegalArgumentException("Employer must be a CorporateCustomer or SMECustomer");
        }
    }

    private void createEmployment(IndividualCustomer employee, Customer employer,
                                   String department, String position, LocalDate startDate) {
        EmploymentRelationship details = createEmploymentDetails(employee, employer, department, position, startDate);
        createEmployment(employee, employer, details);
    }

    private EmploymentRelationship createEmploymentDetails(IndividualCustomer employee, Customer employer,
                                                           String department, String position, LocalDate startDate) {
        return new EmploymentRelationship(
            employee, employer, null, department, position, startDate, EmploymentStatus.ACTIVE
        );
    }
}
