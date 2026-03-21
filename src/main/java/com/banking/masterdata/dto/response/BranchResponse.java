package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.Branch;

import java.time.LocalDateTime;

public class BranchResponse {

    private String code;
    private String name;
    private String countryCode;
    private String address;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BranchResponse() {
    }

    public static BranchResponse fromEntity(Branch branch) {
        BranchResponse response = new BranchResponse();
        response.code = branch.getCode();
        response.name = branch.getName();
        if (branch.getCountry() != null) {
            response.countryCode = branch.getCountry().getIsoCode();
        }
        response.address = branch.getAddress();
        response.active = branch.isActive();
        if (branch.getAudit() != null) {
            response.createdAt = branch.getAudit().getCreatedAt();
            response.updatedAt = branch.getAudit().getUpdatedAt();
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
