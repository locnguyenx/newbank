package com.banking.masterdata.dto.request;

public class UpdateBranchRequest {

    private String name;

    private String address;

    public UpdateBranchRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
