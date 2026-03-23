package com.banking.customer.controller;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.CustomerAuthorization;
import com.banking.customer.domain.enums.AuthorizationStatus;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.RelationshipType;
import com.banking.customer.dto.AuthorizationResponse;
import com.banking.customer.dto.CreateAuthorizationRequest;
import com.banking.customer.dto.UpdateAuthorizationRequest;
import com.banking.customer.exception.AuthorizationNotFoundException;
import com.banking.customer.service.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.common.security.config.TestSecurityConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorizationController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorizationService authorizationService;

    private CorporateCustomer customer;
    private IndividualCustomer authorizedPerson;
    private CustomerAuthorization authorization;
    private AuthorizationResponse response;

    @BeforeEach
    void setUp() {
        customer = new CorporateCustomer("CUST-20240101-100000", "Test Corp", CustomerStatus.ACTIVE);
        customer.setId(1L);

        authorizedPerson = new IndividualCustomer("CUST-20240101-100001", "John Doe", CustomerStatus.ACTIVE);
        authorizedPerson.setId(2L);

        authorization = new CustomerAuthorization(
                customer, authorizedPerson, RelationshipType.AUTHORIZED_SIGNATORY, LocalDate.now());
        authorization.setStatus(AuthorizationStatus.ACTIVE);

        response = new AuthorizationResponse();
        response.setId(1L);
        response.setCustomerId(1L);
        response.setCustomerName("Test Corp");
        response.setAuthorizedPersonId(2L);
        response.setAuthorizedPersonName("John Doe");
        response.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        response.setEffectiveDate(LocalDate.now());
        response.setStatus(AuthorizationStatus.ACTIVE);
        response.setIsPrimary(false);
        response.setDocumentReferences(new ArrayList<>());
    }

    @Test
    void shouldCreateAuthorization() throws Exception {
        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        request.setAuthorizedPersonId(2L);
        request.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        request.setEffectiveDate(LocalDate.now());
        request.setIsPrimary(false);

        when(authorizationService.createAuthorization(eq(1L), any(CreateAuthorizationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/authorizations")
                        .param("customerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.authorizedPersonId").value(2L));
    }

    @Test
    void shouldReturn400WhenCreatingAuthorizationWithMissingAuthorizedPersonId() throws Exception {
        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        request.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        request.setEffectiveDate(LocalDate.now());

        mockMvc.perform(post("/api/authorizations")
                        .param("customerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_001"));
    }

    @Test
    void shouldGetAuthorizationsByCustomer() throws Exception {
        when(authorizationService.getActiveAuthorizations(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/authorizations/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].customerId").value(1L));
    }

    @Test
    void shouldUpdateAuthorization() throws Exception {
        UpdateAuthorizationRequest request = new UpdateAuthorizationRequest();
        request.setRelationshipType(RelationshipType.DIRECTOR);
        request.setIsPrimary(true);

        AuthorizationResponse updatedResponse = new AuthorizationResponse();
        updatedResponse.setId(1L);
        updatedResponse.setCustomerId(1L);
        updatedResponse.setAuthorizedPersonId(2L);
        updatedResponse.setRelationshipType(RelationshipType.DIRECTOR);
        updatedResponse.setIsPrimary(true);
        updatedResponse.setStatus(AuthorizationStatus.ACTIVE);
        updatedResponse.setEffectiveDate(LocalDate.now());

        when(authorizationService.updateAuthorization(eq(1L), any(UpdateAuthorizationRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/authorizations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationshipType").value("DIRECTOR"))
                .andExpect(jsonPath("$.isPrimary").value(true));
    }

    @Test
    void shouldRevokeAuthorization() throws Exception {
        doNothing().when(authorizationService).revokeAuthorization(1L);

        mockMvc.perform(post("/api/authorizations/1/revoke"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenAuthorizationNotFound() throws Exception {
        when(authorizationService.updateAuthorization(eq(999L), any())).thenThrow(new AuthorizationNotFoundException(999L));

        UpdateAuthorizationRequest request = new UpdateAuthorizationRequest();
        request.setRelationshipType(RelationshipType.DIRECTOR);

        mockMvc.perform(put("/api/authorizations/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("AUTH_001"));
    }
}
