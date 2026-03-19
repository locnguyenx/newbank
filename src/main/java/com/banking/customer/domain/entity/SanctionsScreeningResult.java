package com.banking.customer.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "sanctions_screening_results")
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class SanctionsScreeningResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_check_id", nullable = false)
    private KYCCheck kycCheck;

    @Column(name = "screened_at", nullable = false)
    private Instant screenedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private com.banking.customer.domain.enums.SanctionsScreeningResult screeningResult;

    @Column(name = "matched_names", columnDefinition = "TEXT")
    private String matchedNames;

    @Column(name = "is_cleared", nullable = false)
    private Boolean isCleared;

    @Column(name = "cleared_by")
    private String clearedBy;

    @Column(name = "cleared_at")
    private Instant clearedAt;

    protected SanctionsScreeningResult() {
    }

    public SanctionsScreeningResult(KYCCheck kycCheck, Instant screenedAt, 
            com.banking.customer.domain.enums.SanctionsScreeningResult result) {
        this.kycCheck = kycCheck;
        this.screenedAt = screenedAt;
        this.screeningResult = result;
        this.isCleared = false;
    }

    public Long getId() {
        return id;
    }

    public KYCCheck getKycCheck() {
        return kycCheck;
    }

    public Instant getScreenedAt() {
        return screenedAt;
    }

    public void setScreenedAt(Instant screenedAt) {
        this.screenedAt = screenedAt;
    }

    public com.banking.customer.domain.enums.SanctionsScreeningResult getScreeningResult() {
        return screeningResult;
    }

    public void setScreeningResult(com.banking.customer.domain.enums.SanctionsScreeningResult result) {
        this.screeningResult = result;
    }

    public String getMatchedNames() {
        return matchedNames;
    }

    public void setMatchedNames(String matchedNames) {
        this.matchedNames = matchedNames;
    }

    public Boolean getIsCleared() {
        return isCleared;
    }

    public void setIsCleared(Boolean isCleared) {
        this.isCleared = isCleared;
    }

    public String getClearedBy() {
        return clearedBy;
    }

    public void setClearedBy(String clearedBy) {
        this.clearedBy = clearedBy;
    }

    public Instant getClearedAt() {
        return clearedAt;
    }

    public void setClearedAt(Instant clearedAt) {
        this.clearedAt = clearedAt;
    }
}