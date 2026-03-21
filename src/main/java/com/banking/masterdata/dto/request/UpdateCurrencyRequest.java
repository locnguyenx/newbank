package com.banking.masterdata.dto.request;

public class UpdateCurrencyRequest {

    private String name;

    private String symbol;

    public UpdateCurrencyRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
