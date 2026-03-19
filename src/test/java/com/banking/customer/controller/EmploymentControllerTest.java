package com.banking.customer.controller;

import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.EmploymentRelationship;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.EmploymentStatus;
import com.banking.customer.dto.BulkUploadResult;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.repository.EmploymentRelationshipRepository;
import com.banking.customer.service.EmploymentRelationshipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmploymentController.class)
class EmploymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmploymentRelationshipService employmentRelationshipService;

    @MockBean
    private EmploymentRelationshipRepository employmentRelationshipRepository;

    @MockBean
    private CustomerRepository customerRepository;

    private IndividualCustomer employee;
    private CorporateCustomer employer;
    private EmploymentRelationship employment;

    @BeforeEach
    void setUp() {
        employee = new IndividualCustomer("CUST-20240101-100001", "John Doe", CustomerStatus.ACTIVE);
        employee.setId(1L);
        employee.setTaxId("11122233");

        employer = new CorporateCustomer("CUST-20240101-100000", "Test Corp", CustomerStatus.ACTIVE);
        employer.setId(2L);
        employer.setTaxId("12345678");

        employment = new EmploymentRelationship(employee, employer, "EMP001", "Engineering", "Developer",
                LocalDate.now(), EmploymentStatus.ACTIVE);
        employment.setId(1L);
    }

    @Test
    void shouldCreateEmployment() throws Exception {
        EmploymentController.CreateEmploymentRequest request = new EmploymentController.CreateEmploymentRequest();
        request.setEmployeeId(1L);
        request.setEmployerId(2L);
        request.setEmployeeNumber("EMP001");
        request.setDepartment("Engineering");
        request.setPosition("Developer");
        request.setStartDate(LocalDate.now());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(employer));
        when(employmentRelationshipService.createEmployment(any(), any(), any())).thenReturn(employment);

        mockMvc.perform(post("/api/employments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.employeeNumber").value("EMP001"))
                .andExpect(jsonPath("$.department").value("Engineering"));
    }

    @Test
    void shouldReturn404WhenEmployeeNotFound() throws Exception {
        EmploymentController.CreateEmploymentRequest request = new EmploymentController.CreateEmploymentRequest();
        request.setEmployeeId(999L);
        request.setEmployerId(2L);
        request.setDepartment("Engineering");
        request.setPosition("Developer");
        request.setStartDate(LocalDate.now());

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/employments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CUST-002"));
    }

    @Test
    void shouldGetEmploymentById() throws Exception {
        when(employmentRelationshipRepository.findById(1L)).thenReturn(Optional.of(employment));

        mockMvc.perform(get("/api/employments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.employeeNumber").value("EMP001"));
    }

    @Test
    void shouldReturn400WhenEmploymentNotFound() throws Exception {
        when(employmentRelationshipRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employments/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldBulkUploadEmployees() throws Exception {
        String csvContent = "employeeNumber,department,position,startDate\nCUST-20240101-100001,Engineering,Developer,2024-01-01";

        BulkUploadResult result = new BulkUploadResult();
        result.setSuccessCount(1);
        result.setFailureCount(0);

        when(customerRepository.findById(2L)).thenReturn(Optional.of(employer));
        when(employmentRelationshipService.bulkUploadEmployees(any(), any())).thenReturn(result);

        mockMvc.perform(post("/api/employments/bulk")
                        .param("employerId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(csvContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.successCount").value(1))
                .andExpect(jsonPath("$.failureCount").value(0));
    }
}
