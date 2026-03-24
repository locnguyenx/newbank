package com.banking.customer.controller;

import com.banking.customer.dto.CreateCorporateCustomerRequest;
import com.banking.customer.exception.DuplicateCustomerException;
import com.banking.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.common.security.config.TestSecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void shouldHandleCustomerNotFoundException() throws Exception {
        when(customerService.getCustomerById(999L))
                .thenThrow(new com.banking.customer.exception.CustomerNotFoundException(999L));

        mockMvc.perform(get("/api/customers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("CUSTOMER_001"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldHandleDuplicateCustomerException() throws Exception {
        CreateCorporateCustomerRequest request = new CreateCorporateCustomerRequest();
        request.setName("Test");
        request.setTaxId("12345678");

        when(customerService.createCorporate(any(CreateCorporateCustomerRequest.class)))
                .thenThrow(new DuplicateCustomerException("taxId", "12345678"));

        mockMvc.perform(post("/api/customers/corporate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messageCode").value("CUSTOMER_002"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldHandleValidationException() throws Exception {
        CreateCorporateCustomerRequest request = new CreateCorporateCustomerRequest();
        request.setName(null);
        request.setTaxId(null);

        mockMvc.perform(post("/api/customers/corporate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_001"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void shouldHandleIllegalArgumentException() throws Exception {
        when(customerService.getCustomerById(1L))
                .thenThrow(new IllegalArgumentException("Invalid customer ID"));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_001"))
                .andExpect(jsonPath("$.message").value("Invalid customer ID"));
    }
}
