package com.banking.customer.service;

import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.CustomerAuthorization;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.enums.AuthorizationStatus;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.RelationshipType;
import com.banking.customer.dto.AuthorizationResponse;
import com.banking.customer.dto.CreateAuthorizationRequest;
import com.banking.customer.dto.UpdateAuthorizationRequest;
import com.banking.customer.exception.AuthorizationNotFoundException;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.repository.CustomerAuthorizationRepository;
import com.banking.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private CustomerAuthorizationRepository authorizationRepository;

    @Mock
    private CustomerRepository customerRepository;

    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        authorizationService = new AuthorizationService(authorizationRepository, customerRepository);
    }

    @Test
    void shouldCreateAuthorization() {
        Customer customer = createCustomer(1L, "Test Corp");
        IndividualCustomer authorizedPerson = createIndividualCustomer(2L, "John Doe");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(authorizedPerson));
        when(authorizationRepository.save(any(CustomerAuthorization.class)))
            .thenAnswer(invocation -> {
                CustomerAuthorization auth = invocation.getArgument(0);
                setAuthorizationId(auth, 1L);
                return auth;
            });

        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        request.setAuthorizedPersonId(2L);
        request.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        request.setEffectiveDate(LocalDate.now());
        request.setIsPrimary(true);
        request.setDocumentReference("DOC-001");

        AuthorizationResponse response = authorizationService.createAuthorization(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getCustomerId());
        assertEquals(2L, response.getAuthorizedPersonId());
        assertEquals(RelationshipType.AUTHORIZED_SIGNATORY, response.getRelationshipType());
        assertEquals(AuthorizationStatus.ACTIVE, response.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        request.setAuthorizedPersonId(2L);
        request.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        request.setEffectiveDate(LocalDate.now());

        assertThrows(CustomerNotFoundException.class, () -> 
            authorizationService.createAuthorization(1L, request));
    }

    @Test
    void shouldUpdateAuthorization() {
        CustomerAuthorization authorization = createAuthorization(1L, createCustomer(1L, "Test Corp"), 
            createIndividualCustomer(2L, "John Doe"), RelationshipType.DIRECTOR);

        when(authorizationRepository.findById(1L)).thenReturn(Optional.of(authorization));
        when(authorizationRepository.save(any(CustomerAuthorization.class))).thenReturn(authorization);

        UpdateAuthorizationRequest request = new UpdateAuthorizationRequest();
        request.setRelationshipType(RelationshipType.OWNER);
        request.setIsPrimary(true);

        AuthorizationResponse response = authorizationService.updateAuthorization(1L, request);

        assertNotNull(response);
        verify(authorizationRepository).save(any(CustomerAuthorization.class));
    }

    @Test
    void shouldThrowExceptionWhenAuthorizationNotFoundForUpdate() {
        when(authorizationRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateAuthorizationRequest request = new UpdateAuthorizationRequest();
        request.setRelationshipType(RelationshipType.OWNER);

        assertThrows(AuthorizationNotFoundException.class, () -> 
            authorizationService.updateAuthorization(1L, request));
    }

    @Test
    void shouldRevokeAuthorization() {
        CustomerAuthorization authorization = createAuthorization(1L, createCustomer(1L, "Test Corp"), 
            createIndividualCustomer(2L, "John Doe"), RelationshipType.AUTHORIZED_SIGNATORY);

        when(authorizationRepository.findById(1L)).thenReturn(Optional.of(authorization));

        authorizationService.revokeAuthorization(1L);

        assertEquals(AuthorizationStatus.REVOKED, authorization.getStatus());
        verify(authorizationRepository).save(authorization);
    }

    @Test
    void shouldThrowExceptionWhenAuthorizationNotFoundForRevoke() {
        when(authorizationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuthorizationNotFoundException.class, () -> 
            authorizationService.revokeAuthorization(1L));
    }

    @Test
    void shouldGetActiveAuthorizations() {
        Customer customer = createCustomer(1L, "Test Corp");
        IndividualCustomer authorizedPerson = createIndividualCustomer(2L, "John Doe");
        
        CustomerAuthorization auth1 = createAuthorization(1L, customer, authorizedPerson, RelationshipType.AUTHORIZED_SIGNATORY);
        CustomerAuthorization auth2 = createAuthorization(2L, customer, authorizedPerson, RelationshipType.DIRECTOR);

        when(authorizationRepository.findByCustomerIdAndStatus(1L, AuthorizationStatus.ACTIVE))
            .thenReturn(Arrays.asList(auth1, auth2));

        List<AuthorizationResponse> responses = authorizationService.getActiveAuthorizations(1L);

        assertEquals(2, responses.size());
    }

    @Test
    void shouldGetExpiringAuthorizations() {
        Customer customer = createCustomer(1L, "Test Corp");
        IndividualCustomer authorizedPerson = createIndividualCustomer(2L, "John Doe");
        
        CustomerAuthorization expiringAuth = createAuthorization(1L, customer, authorizedPerson, RelationshipType.AUTHORIZED_SIGNATORY);
        expiringAuth.setExpirationDate(LocalDate.now().plusDays(5));

        when(authorizationRepository.findAll()).thenReturn(Arrays.asList(expiringAuth));

        List<AuthorizationResponse> responses = authorizationService.getExpiringAuthorizations(30);

        assertTrue(responses.size() >= 1);
    }

    private Customer createCustomer(Long id, String name) {
        Customer customer = new IndividualCustomer("CUST-" + id, name, CustomerStatus.ACTIVE);
        customer.setId(id);
        return customer;
    }

    private IndividualCustomer createIndividualCustomer(Long id, String name) {
        IndividualCustomer customer = new IndividualCustomer("CUST-" + id, name, CustomerStatus.ACTIVE);
        customer.setId(id);
        return customer;
    }

    private CustomerAuthorization createAuthorization(Long id, Customer customer, 
            IndividualCustomer authorizedPerson, RelationshipType relationshipType) {
        CustomerAuthorization authorization = new CustomerAuthorization(customer, authorizedPerson, 
            relationshipType, LocalDate.now());
        setAuthorizationId(authorization, id);
        return authorization;
    }

    private void setAuthorizationId(CustomerAuthorization auth, Long id) {
        try {
            var idField = CustomerAuthorization.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(auth, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set id", e);
        }
    }
}