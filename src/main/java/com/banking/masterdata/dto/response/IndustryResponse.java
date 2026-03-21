package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.Industry;

import java.time.LocalDateTime;

public class IndustryResponse {

    private String code;
    private String name;
    private String parentCode;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public IndustryResponse() {
    }

    public static IndustryResponse fromEntity(Industry industry) {
        IndustryResponse response = new IndustryResponse();
        response.code = industry.getCode();
        response.name = industry.getName();
        response.parentCode = industry.getParentCode();
        response.active = industry.isActive();
        if (industry.getAudit() != null) {
            response.createdAt = industry.getAudit().getCreatedAt();
            response.updatedAt = industry.getAudit().getUpdatedAt();
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
