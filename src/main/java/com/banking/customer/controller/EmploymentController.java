package com.banking.customer.controller;

import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.EmploymentRelationship;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.enums.EmploymentStatus;
import com.banking.customer.dto.BulkUploadResult;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.repository.EmploymentRelationshipRepository;
import com.banking.customer.service.EmploymentRelationshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employments")
public class EmploymentController {

    private final EmploymentRelationshipService employmentRelationshipService;
    private final EmploymentRelationshipRepository employmentRelationshipRepository;
    private final CustomerRepository customerRepository;

    public EmploymentController(EmploymentRelationshipService employmentRelationshipService,
                               EmploymentRelationshipRepository employmentRelationshipRepository,
                               CustomerRepository customerRepository) {
        this.employmentRelationshipService = employmentRelationshipService;
        this.employmentRelationshipRepository = employmentRelationshipRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<EmploymentResponse> createEmployment(@RequestBody CreateEmploymentRequest request) {
        Customer employee = customerRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getEmployeeId()));
        
        Customer employer = customerRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getEmployerId()));

        EmploymentRelationship details = new EmploymentRelationship(
                (IndividualCustomer) employee, employer, request.getEmployeeNumber(),
                request.getDepartment(), request.getPosition(), request.getStartDate(),
                com.banking.customer.domain.enums.EmploymentStatus.ACTIVE);

        EmploymentRelationship created = employmentRelationshipService.createEmployment(
                (IndividualCustomer) employee, employer, details);

        EmploymentResponse response = toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmploymentResponse> getEmploymentById(@PathVariable Long id) {
        EmploymentRelationship employment = employmentRelationshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment relationship not found: " + id));
        EmploymentResponse response = toResponse(employment);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkUploadResult> bulkUploadEmployees(
            @RequestParam Long employerId,
            @RequestBody String csvContent) {
        
        Customer employer = customerRepository.findById(employerId)
                .orElseThrow(() -> new CustomerNotFoundException(employerId));

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(employer, csvContent);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    private EmploymentResponse toResponse(EmploymentRelationship employment) {
        EmploymentResponse response = new EmploymentResponse();
        response.setId(employment.getId());
        response.setEmployeeId(employment.getEmployee().getId());
        response.setEmployeeName(employment.getEmployee().getName());
        response.setEmployerId(employment.getEmployer().getId());
        response.setEmployerName(employment.getEmployer().getName());
        response.setEmployeeNumber(employment.getEmployeeNumber());
        response.setDepartment(employment.getDepartment());
        response.setPosition(employment.getPosition());
        response.setStartDate(employment.getStartDate());
        response.setEndDate(employment.getEndDate());
        response.setStatus(employment.getStatus());
        return response;
    }

    public static class CreateEmploymentRequest {
        private Long employeeId;
        private Long employerId;
        private String employeeNumber;
        private String department;
        private String position;
        private java.time.LocalDate startDate;

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public Long getEmployerId() {
            return employerId;
        }

        public void setEmployerId(Long employerId) {
            this.employerId = employerId;
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

        public java.time.LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(java.time.LocalDate startDate) {
            this.startDate = startDate;
        }
    }

    public static class EmploymentResponse {
        private Long id;
        private Long employeeId;
        private String employeeName;
        private Long employerId;
        private String employerName;
        private String employeeNumber;
        private String department;
        private String position;
        private java.time.LocalDate startDate;
        private java.time.LocalDate endDate;
        private EmploymentStatus status;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public Long getEmployerId() {
            return employerId;
        }

        public void setEmployerId(Long employerId) {
            this.employerId = employerId;
        }

        public String getEmployerName() {
            return employerName;
        }

        public void setEmployerName(String employerName) {
            this.employerName = employerName;
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

        public java.time.LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(java.time.LocalDate startDate) {
            this.startDate = startDate;
        }

        public java.time.LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(java.time.LocalDate endDate) {
            this.endDate = endDate;
        }

        public EmploymentStatus getStatus() {
            return status;
        }

        public void setStatus(EmploymentStatus status) {
            this.status = status;
        }
    }
}
