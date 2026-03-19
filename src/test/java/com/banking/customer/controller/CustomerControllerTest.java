package com.banking.customer.controller;

import com.banking.customer.dto.CreateCorporateCustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.dto.UpdateCustomerRequest;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.exception.DuplicateCustomerException;
import com.banking.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void shouldCreateCorporateCustomer() throws Exception {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        CustomerResponse response = createCustomerResponse();

        when(customerService.createCorporate(any())).thenReturn(response);

        mockMvc.perform(post("/api/customers/corporate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerNumber").value("CUST-20240101-100000"))
                .andExpect(jsonPath("$.name").value("Test Corp"));
    }

    @Test
    void shouldReturn400WhenCreatingCorporateCustomerWithMissingName() throws Exception {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        request.setName(null);

        mockMvc.perform(post("/api/customers/corporate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
    }

    @Test
    void shouldReturn400WhenCreatingCorporateCustomerWithMissingTaxId() throws Exception {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        request.setTaxId(null);

        mockMvc.perform(post("/api/customers/corporate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("taxId"));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        CustomerResponse response = createCustomerResponse();

        when(customerService.getCustomerById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerNumber").value("CUST-20240101-100000"));
    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        when(customerService.getCustomerById(999L)).thenThrow(new CustomerNotFoundException(999L));

        mockMvc.perform(get("/api/customers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CUST-002"));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Corp");

        CustomerResponse response = createCustomerResponse();
        response.setName("Updated Corp");

        when(customerService.updateCustomer(eq(1L), any(UpdateCustomerRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Corp"));
    }

    @Test
    void shouldReturn409WhenDuplicateCustomer() throws Exception {
        CreateCorporateCustomerRequest request = createCorporateRequest();

        when(customerService.createCorporate(any())).thenThrow(new DuplicateCustomerException("taxId", "12345678"));

        mockMvc.perform(post("/api/customers/corporate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("CUST-001"));
    }

    @Test
    void shouldSearchCustomers() throws Exception {
        CustomerResponse response = createCustomerResponse();
        Page<CustomerResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        when(customerService.searchCustomers(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/customers/search")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    private CreateCorporateCustomerRequest createCorporateRequest() {
        CreateCorporateCustomerRequest request = new CreateCorporateCustomerRequest();
        request.setName("Test Corp");
        request.setTaxId("12345678");
        request.setRegistrationNumber("REG001");
        request.setIndustry("Technology");
        return request;
    }

    private CustomerResponse createCustomerResponse() {
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);
        response.setCustomerNumber("CUST-20240101-100000");
        response.setName("Test Corp");
        response.setTaxId("12345678");
        response.setRegistrationNumber("REG001");
        response.setIndustry("Technology");
        response.setStatus(com.banking.customer.domain.enums.CustomerStatus.PENDING);
        response.setType(com.banking.customer.domain.enums.CustomerType.CORPORATE);
        return response;
    }
}
