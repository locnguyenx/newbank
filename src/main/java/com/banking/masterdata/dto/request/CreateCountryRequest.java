package com.banking.masterdata.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateCountryRequest {

    @NotBlank(message = "Country ISO code is required")
    private String isoCode;

    @NotBlank(message = "Country name is required")
    private String name;

    private String region;

    public CreateCountryRequest() {
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
}
