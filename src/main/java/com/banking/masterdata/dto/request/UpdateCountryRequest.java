package com.banking.masterdata.dto.request;

public class UpdateCountryRequest {

    private String name;

    private String region;

    public UpdateCountryRequest() {
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
