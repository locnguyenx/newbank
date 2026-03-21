package com.banking.charges.dto.request;

public class UpdateChargeDefinitionRequest {

    private String name;
    private String chargeType;
    private String currency;

    public UpdateChargeDefinitionRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}