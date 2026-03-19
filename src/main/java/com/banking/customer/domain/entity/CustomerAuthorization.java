package com.banking.customer.domain.entity;

import com.banking.customer.domain.enums.AuthorizationStatus;
import com.banking.customer.domain.enums.RelationshipType;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_authorizations")
@EntityListeners(AuditingEntityListener.class)
public class CustomerAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorized_person_id", nullable = false)
    private IndividualCustomer authorizedPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false)
    private RelationshipType relationshipType;

    @OneToMany(mappedBy = "authorization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorizationDocument> documents = new ArrayList<>();

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuthorizationStatus status = AuthorizationStatus.ACTIVE;

    protected CustomerAuthorization() {
    }

    public CustomerAuthorization(Customer customer, IndividualCustomer authorizedPerson, RelationshipType relationshipType, LocalDate effectiveDate) {
        this.customer = customer;
        this.authorizedPerson = authorizedPerson;
        this.relationshipType = relationshipType;
        this.effectiveDate = effectiveDate;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IndividualCustomer getAuthorizedPerson() {
        return authorizedPerson;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public List<AuthorizationDocument> getDocuments() {
        return documents;
    }

    public void addDocument(AuthorizationDocument document) {
        documents.add(document);
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
}
