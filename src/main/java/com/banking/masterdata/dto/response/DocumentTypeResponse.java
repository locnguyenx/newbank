package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.DocumentType;

import java.time.LocalDateTime;

public class DocumentTypeResponse {

    private String code;
    private String name;
    private String category;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DocumentTypeResponse() {
    }

    public static DocumentTypeResponse fromEntity(DocumentType documentType) {
        DocumentTypeResponse response = new DocumentTypeResponse();
        response.code = documentType.getCode();
        response.name = documentType.getName();
        response.category = documentType.getCategory();
        response.active = documentType.isActive();
        if (documentType.getAudit() != null) {
            response.createdAt = documentType.getAudit().getCreatedAt();
            response.updatedAt = documentType.getAudit().getUpdatedAt();
        }
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
