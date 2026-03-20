package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductAuditLog;
import com.banking.product.domain.enums.AuditAction;
import com.banking.product.domain.enums.ProductStatus;
import java.time.LocalDateTime;

public class ProductAuditLogResponse {

    private Long id;
    private String action;
    private String actor;
    private String fromStatus;
    private String toStatus;
    private String comment;
    private String makerUsername;
    private LocalDateTime timestamp;

    public ProductAuditLogResponse() {
    }

    public static ProductAuditLogResponse fromEntity(ProductAuditLog auditLog) {
        ProductAuditLogResponse response = new ProductAuditLogResponse();
        response.id = auditLog.getId();
        response.action = auditLog.getAction() != null ? auditLog.getAction().name() : null;
        response.actor = auditLog.getActor();
        response.fromStatus = auditLog.getFromStatus() != null ? auditLog.getFromStatus().name() : null;
        response.toStatus = auditLog.getToStatus() != null ? auditLog.getToStatus().name() : null;
        response.comment = auditLog.getComment();
        response.makerUsername = auditLog.getMakerUsername();
        response.timestamp = auditLog.getTimestamp();
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
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