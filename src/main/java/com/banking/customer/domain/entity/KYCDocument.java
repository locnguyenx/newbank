package com.banking.customer.domain.entity;

import com.banking.customer.domain.enums.DocumentVerificationStatus;
import com.banking.customer.domain.enums.KYCDocumentType;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "kyc_documents")
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class KYCDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_check_id", nullable = false)
    private KYCCheck kycCheck;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private KYCDocumentType documentType;

    @Column(name = "document_reference", nullable = false)
    private String documentReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private DocumentVerificationStatus verificationStatus;

    @Column(name = "verified_by")
    private String verifiedBy;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    protected KYCDocument() {
    }

    public KYCDocument(KYCCheck kycCheck, KYCDocumentType documentType, String documentReference) {
        this.kycCheck = kycCheck;
        this.documentType = documentType;
        this.documentReference = documentReference;
        this.verificationStatus = DocumentVerificationStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public KYCCheck getKycCheck() {
        return kycCheck;
    }

    public KYCDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(KYCDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    public DocumentVerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(DocumentVerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}