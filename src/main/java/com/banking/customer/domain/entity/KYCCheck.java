package com.banking.customer.domain.entity;

import com.banking.customer.domain.embeddable.AuditFields;
import com.banking.customer.domain.enums.KYCLevel;
import com.banking.customer.domain.enums.KYCStatus;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;

@Entity
@Table(name = "kyc_checks")
@EntityListeners(AuditingEntityListener.class)
public class KYCCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_level", nullable = false)
    private KYCLevel kycLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private KYCStatus status;

    @Column(name = "assigned_officer")
    private String assignedOfficer;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "next_review_date")
    private Instant nextReviewDate;

    @Embedded
    private AuditFields auditFields;

    protected KYCCheck() {
    }

    public KYCCheck(Customer customer, KYCLevel kycLevel, KYCStatus status) {
        this.customer = customer;
        this.kycLevel = kycLevel;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public KYCLevel getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(KYCLevel kycLevel) {
        this.kycLevel = kycLevel;
    }

    public KYCStatus getStatus() {
        return status;
    }

    public void setStatus(KYCStatus status) {
        this.status = status;
    }

    public String getAssignedOfficer() {
        return assignedOfficer;
    }

    public void setAssignedOfficer(String assignedOfficer) {
        this.assignedOfficer = assignedOfficer;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Instant nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public AuditFields getAuditFields() {
        return auditFields;
    }
}