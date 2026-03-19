package com.banking.customer.dto;

import com.banking.customer.domain.enums.RelationshipType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateAuthorizationRequest {

    @NotNull(message = "Authorized person ID is required")
    private Long authorizedPersonId;

    @NotNull(message = "Relationship type is required")
    private RelationshipType relationshipType;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    private LocalDate expirationDate;

    private Boolean isPrimary = false;

    private String documentReference;

    public Long getAuthorizedPersonId() {
        return authorizedPersonId;
    }

    public void setAuthorizedPersonId(Long authorizedPersonId) {
        this.authorizedPersonId = authorizedPersonId;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
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

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }
}