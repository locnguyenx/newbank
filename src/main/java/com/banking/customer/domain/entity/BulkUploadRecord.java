package com.banking.customer.domain.entity;

import com.banking.customer.domain.embeddable.AuditFields;
import com.banking.customer.domain.enums.BulkUploadStatus;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "bulk_upload_records")
@EntityListeners(AuditingEntityListener.class)
public class BulkUploadRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Customer employer;

    @Column(name = "uploaded_by", nullable = false)
    private String uploadedBy;

    @Column(name = "total_records", nullable = false)
    private Integer totalRecords;

    @Column(name = "processed_records", nullable = false)
    private Integer processedRecords;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BulkUploadStatus status;

    @Embedded
    private AuditFields auditFields;

    protected BulkUploadRecord() {
    }

    public BulkUploadRecord(Customer employer, String uploadedBy, Integer totalRecords) {
        this.employer = employer;
        this.uploadedBy = uploadedBy;
        this.totalRecords = totalRecords;
        this.processedRecords = 0;
        this.status = BulkUploadStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public Customer getEmployer() {
        return employer;
    }

    public void setEmployer(Customer employer) {
        this.employer = employer;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getProcessedRecords() {
        return processedRecords;
    }

    public void setProcessedRecords(Integer processedRecords) {
        this.processedRecords = processedRecords;
    }

    public void incrementProcessedRecords() {
        this.processedRecords++;
    }

    public BulkUploadStatus getStatus() {
        return status;
    }

    public void setStatus(BulkUploadStatus status) {
        this.status = status;
    }

    public AuditFields getAuditFields() {
        return auditFields;
    }
}