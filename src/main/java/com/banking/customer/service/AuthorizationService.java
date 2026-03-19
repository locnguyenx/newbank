package com.banking.customer.service;

import com.banking.customer.domain.entity.AuthorizationDocument;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.CustomerAuthorization;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.enums.AuthorizationStatus;
import com.banking.customer.domain.enums.DocumentType;
import com.banking.customer.dto.AuthorizationResponse;
import com.banking.customer.dto.CreateAuthorizationRequest;
import com.banking.customer.dto.UpdateAuthorizationRequest;
import com.banking.customer.exception.AuthorizationNotFoundException;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.repository.CustomerAuthorizationRepository;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    private final CustomerAuthorizationRepository authorizationRepository;
    private final CustomerRepository customerRepository;

    public AuthorizationService(CustomerAuthorizationRepository authorizationRepository,
                                  CustomerRepository customerRepository) {
        this.authorizationRepository = authorizationRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public AuthorizationResponse createAuthorization(Long customerId, CreateAuthorizationRequest request) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));

        IndividualCustomer authorizedPerson = (IndividualCustomer) customerRepository.findById(request.getAuthorizedPersonId())
            .orElseThrow(() -> new CustomerNotFoundException(request.getAuthorizedPersonId()));

        if (!(authorizedPerson instanceof IndividualCustomer)) {
            throw new IllegalArgumentException("Authorized person must be an individual customer");
        }

        CustomerAuthorization authorization = new CustomerAuthorization(
            customer,
            (IndividualCustomer) authorizedPerson,
            request.getRelationshipType(),
            request.getEffectiveDate()
        );

        authorization.setExpirationDate(request.getExpirationDate());
        authorization.setIsPrimary(request.getIsPrimary());

        if (request.getDocumentReference() != null && !request.getDocumentReference().isBlank()) {
            addDocumentReference(authorization, request.getDocumentReference());
        }

        CustomerAuthorization saved = authorizationRepository.save(authorization);
        return toResponse(saved);
    }

    @Transactional
    public AuthorizationResponse updateAuthorization(Long authorizationId, UpdateAuthorizationRequest request) {
        CustomerAuthorization authorization = authorizationRepository.findById(authorizationId)
            .orElseThrow(() -> new AuthorizationNotFoundException(authorizationId));

        if (request.getRelationshipType() != null) {
            authorization.setRelationshipType(request.getRelationshipType());
        }

        if (request.getEffectiveDate() != null) {
            authorization.setEffectiveDate(request.getEffectiveDate());
        }

        if (request.getExpirationDate() != null) {
            authorization.setExpirationDate(request.getExpirationDate());
        }

        if (request.getIsPrimary() != null) {
            authorization.setIsPrimary(request.getIsPrimary());
        }

        if (request.getDocumentReference() != null && !request.getDocumentReference().isBlank()) {
            addDocumentReference(authorization, request.getDocumentReference());
        }

        CustomerAuthorization saved = authorizationRepository.save(authorization);
        return toResponse(saved);
    }

    @Transactional
    public void revokeAuthorization(Long authorizationId) {
        CustomerAuthorization authorization = authorizationRepository.findById(authorizationId)
            .orElseThrow(() -> new AuthorizationNotFoundException(authorizationId));

        authorization.setStatus(AuthorizationStatus.REVOKED);
        authorizationRepository.save(authorization);
    }

    @Transactional(readOnly = true)
    public List<AuthorizationResponse> getActiveAuthorizations(Long customerId) {
        List<CustomerAuthorization> authorizations = authorizationRepository
            .findByCustomerIdAndStatus(customerId, AuthorizationStatus.ACTIVE);
        
        return authorizations.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuthorizationResponse> getExpiringAuthorizations(int daysAhead) {
        LocalDate expirationThreshold = LocalDate.now().plusDays(daysAhead);
        
        List<CustomerAuthorization> allActive = authorizationRepository
            .findByCustomerIdAndStatus(null, AuthorizationStatus.ACTIVE);
        
        if (allActive.isEmpty()) {
            return authorizationRepository.findAll().stream()
                .filter(a -> a.getStatus() == AuthorizationStatus.ACTIVE)
                .filter(a -> a.getExpirationDate() != null)
                .filter(a -> a.getExpirationDate().isBefore(expirationThreshold))
                .map(this::toResponse)
                .collect(Collectors.toList());
        }
        
        return authorizationRepository.findAll().stream()
            .filter(a -> a.getStatus() == AuthorizationStatus.ACTIVE)
            .filter(a -> a.getExpirationDate() != null)
            .filter(a -> !a.getExpirationDate().isAfter(expirationThreshold))
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private void addDocumentReference(CustomerAuthorization authorization, String documentReference) {
        AuthorizationDocument document = new AuthorizationDocument(
            authorization,
            DocumentType.POWER_OF_ATTORNEY,
            documentReference
        );
        authorization.addDocument(document);
    }

    private AuthorizationResponse toResponse(CustomerAuthorization authorization) {
        AuthorizationResponse response = new AuthorizationResponse();
        response.setId(authorization.getId());
        response.setCustomerId(authorization.getCustomer().getId());
        response.setCustomerName(authorization.getCustomer().getName());
        response.setAuthorizedPersonId(authorization.getAuthorizedPerson().getId());
        response.setAuthorizedPersonName(authorization.getAuthorizedPerson().getName());
        response.setRelationshipType(authorization.getRelationshipType());
        response.setIsPrimary(authorization.getIsPrimary());
        response.setEffectiveDate(authorization.getEffectiveDate());
        response.setExpirationDate(authorization.getExpirationDate());
        response.setStatus(authorization.getStatus());
        
        List<String> documentRefs = authorization.getDocuments().stream()
            .map(AuthorizationDocument::getDocumentReference)
            .collect(Collectors.toList());
        response.setDocumentReferences(documentRefs);
        
        return response;
    }
}