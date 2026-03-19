package com.banking.customer.dto;

import com.banking.customer.domain.enums.AuthorizationStatus;
import com.banking.customer.domain.enums.RelationshipType;
import java.time.LocalDate;
import java.util.List;

public class AuthorizationResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long authorizedPersonId;
    private String authorizedPersonName;
    private RelationshipType relationshipType;
    private Boolean isPrimary;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private AuthorizationStatus status;
    private List<String> documentReferences;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getAuthorizedPersonId() {
        return authorizedPersonId;
    }

    public void setAuthorizedPersonId(Long authorizedPersonId) {
        this.authorizedPersonId = authorizedPersonId;
    }

    public String getAuthorizedPersonName() {
        return authorizedPersonName;
    }

    public void setAuthorizedPersonName(String authorizedPersonName) {
        this.authorizedPersonName = authorizedPersonName;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public AuthorizationStatus getStatus() {
        return status;
    }

    public void setStatus(AuthorizationStatus status) {
        this.status = status;
    }

    public List<String> getDocumentReferences() {
        return documentReferences;
    }

    public void setDocumentReferences(List<String> documentReferences) {
        this.documentReferences = documentReferences;
    }
}