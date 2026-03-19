package com.banking.customer.service;

import com.banking.customer.domain.entity.CorporateCustomer;
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
import com.banking.customer.repository.CustomerAuthorizationRepository;
import com.banking.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthorizationServiceIntegrationTest {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CustomerAuthorizationRepository authorizationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;
    private IndividualCustomer authorizedPerson;

    @BeforeEach
    void setUp() {
        authorizationRepository.deleteAll();
        customerRepository.deleteAll();

        testCustomer = new CorporateCustomer("CUST-AUTH-001", "Test Corporation", CustomerStatus.ACTIVE);
        testCustomer.setTaxId("TAX-AUTH-001");
        testCustomer = customerRepository.save(testCustomer);

        authorizedPerson = new IndividualCustomer("CUST-AUTH-002", "John Doe", CustomerStatus.ACTIVE);
        authorizedPerson.setTaxId("TAX-AUTH-002");
        authorizedPerson = customerRepository.save(authorizedPerson);
    }

    @Test
    void shouldCreateAuthorization() {
        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        request.setAuthorizedPersonId(authorizedPerson.getId());
        request.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        request.setEffectiveDate(LocalDate.now());
        request.setExpirationDate(LocalDate.now().plusYears(1));
        request.setIsPrimary(true);
        request.setDocumentReference("DOC-AUTH-001");

        AuthorizationResponse response = authorizationService.createAuthorization(testCustomer.getId(), request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(testCustomer.getId(), response.getCustomerId());
        assertEquals(authorizedPerson.getId(), response.getAuthorizedPersonId());
        assertEquals(RelationshipType.AUTHORIZED_SIGNATORY, response.getRelationshipType());
        assertEquals(AuthorizationStatus.ACTIVE, response.getStatus());
        assertTrue(response.getIsPrimary());
        assertEquals(1, response.getDocumentReferences().size());
        assertEquals("DOC-AUTH-001", response.getDocumentReferences().get(0));
    }

    @Test
    void shouldUpdateAuthorization() {
        CreateAuthorizationRequest createRequest = new CreateAuthorizationRequest();
        createRequest.setAuthorizedPersonId(authorizedPerson.getId());
        createRequest.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        createRequest.setEffectiveDate(LocalDate.now());
        createRequest.setIsPrimary(false);

        AuthorizationResponse created = authorizationService.createAuthorization(testCustomer.getId(), createRequest);

        UpdateAuthorizationRequest updateRequest = new UpdateAuthorizationRequest();
        updateRequest.setRelationshipType(RelationshipType.OWNER);
        updateRequest.setIsPrimary(true);
        updateRequest.setDocumentReference("DOC-AUTH-UPDATED");

        AuthorizationResponse updated = authorizationService.updateAuthorization(created.getId(), updateRequest);

        assertNotNull(updated);
        assertEquals(RelationshipType.OWNER, updated.getRelationshipType());
        assertTrue(updated.getIsPrimary());
        assertEquals(1, updated.getDocumentReferences().size());
    }

    @Test
    void shouldRevokeAuthorization() {
        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        request.setAuthorizedPersonId(authorizedPerson.getId());
        request.setRelationshipType(RelationshipType.DIRECTOR);
        request.setEffectiveDate(LocalDate.now());

        AuthorizationResponse created = authorizationService.createAuthorization(testCustomer.getId(), request);

        authorizationService.revokeAuthorization(created.getId());

        AuthorizationResponse revoked = authorizationService.updateAuthorization(created.getId(), new UpdateAuthorizationRequest());
        assertEquals(AuthorizationStatus.REVOKED, revoked.getStatus());
    }

    @Test
    void shouldGetActiveAuthorizations() {
        CreateAuthorizationRequest request1 = new CreateAuthorizationRequest();
        request1.setAuthorizedPersonId(authorizedPerson.getId());
        request1.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        request1.setEffectiveDate(LocalDate.now());

        authorizationService.createAuthorization(testCustomer.getId(), request1);

        CreateAuthorizationRequest request2 = new CreateAuthorizationRequest();
        request2.setAuthorizedPersonId(authorizedPerson.getId());
        request2.setRelationshipType(RelationshipType.DIRECTOR);
        request2.setEffectiveDate(LocalDate.now());

        authorizationService.createAuthorization(testCustomer.getId(), request2);

        List<AuthorizationResponse> activeAuthorizations = authorizationService.getActiveAuthorizations(testCustomer.getId());

        assertEquals(2, activeAuthorizations.size());
        activeAuthorizations.forEach(auth -> assertEquals(AuthorizationStatus.ACTIVE, auth.getStatus()));
    }

    @Test
    void shouldGetExpiringAuthorizations() {
        CreateAuthorizationRequest request = new CreateAuthorizationRequest();
        request.setAuthorizedPersonId(authorizedPerson.getId());
        request.setRelationshipType(RelationshipType.AUTHORIZED_SIGNATORY);
        request.setEffectiveDate(LocalDate.now().minusMonths(6));
        request.setExpirationDate(LocalDate.now().plusDays(15));

        authorizationService.createAuthorization(testCustomer.getId(), request);

        List<AuthorizationResponse> expiring = authorizationService.getExpiringAuthorizations(30);

        assertTrue(expiring.size() >= 1);
    }

    @Test
    void shouldThrowExceptionWhenAuthorizationNotFound() {
        assertThrows(AuthorizationNotFoundException.class, () -> 
            authorizationService.updateAuthorization(999L, new UpdateAuthorizationRequest()));
    }
}