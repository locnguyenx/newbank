package com.banking.customer.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCustomerRequest {

    @NotBlank(message = "Name is required")
    private String name;
    private String taxId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }
}
