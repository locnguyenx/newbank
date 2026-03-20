package com.banking.product.domain.entity;

import com.banking.product.domain.enums.AuditAction;
import com.banking.product.domain.enums.ProductStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_audit_logs", indexes = {
    @Index(name = "idx_product_audit_product_version", columnList = "product_version_id"),
    @Index(name = "idx_product_audit_timestamp", columnList = "timestamp")
})
public class ProductAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_version_id", nullable = false)
    private ProductVersion productVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuditAction action;

    @Column(nullable = false, length = 100)
    private String actor;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private ProductStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", length = 30)
    private ProductStatus toStatus;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "maker_username", length = 100)
    private String makerUsername;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    protected ProductAuditLog() {
    }

    public ProductAuditLog(ProductVersion productVersion, AuditAction action, String actor,
                           ProductStatus fromStatus, ProductStatus toStatus, String comment,
                           String makerUsername) {
        this.productVersion = productVersion;
        this.action = action;
        this.actor = actor;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.comment = comment;
        this.makerUsername = makerUsername;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public ProductVersion getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(ProductVersion productVersion) {
        this.productVersion = productVersion;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public ProductStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(ProductStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public ProductStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(ProductStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMakerUsername() {
        return makerUsername;
    }

    public void setMakerUsername(String makerUsername) {
        this.makerUsername = makerUsername;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
