package com.banking.customer.domain.entity;

import com.banking.customer.domain.enums.DocumentType;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "authorization_documents")
public class AuthorizationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorization_id", nullable = false)
    private CustomerAuthorization authorization;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_reference", nullable = false)
    private String documentReference;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    protected AuthorizationDocument() {
    }

    public AuthorizationDocument(CustomerAuthorization authorization, DocumentType documentType, String documentReference) {
        this.authorization = authorization;
        this.documentType = documentType;
        this.documentReference = documentReference;
        this.uploadedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public CustomerAuthorization getAuthorization() {
        return authorization;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }
}
