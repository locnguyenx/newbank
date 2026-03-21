package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.Country;

import java.time.LocalDateTime;

public class CountryResponse {

    private String isoCode;
    private String name;
    private String region;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CountryResponse() {
    }

    public static CountryResponse fromEntity(Country country) {
        CountryResponse response = new CountryResponse();
        response.isoCode = country.getIsoCode();
        response.name = country.getName();
        response.region = country.getRegion();
        response.active = country.isActive();
        if (country.getAudit() != null) {
            response.createdAt = country.getAudit().getCreatedAt();
            response.updatedAt = country.getAudit().getUpdatedAt();
        }
        return response;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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
