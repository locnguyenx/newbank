package com.banking.customer.dto;

import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.CustomerType;

public class CustomerSearchCriteria {

    private String name;
    private String customerNumber;
    private String taxId;
    private CustomerType type;
    private CustomerStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }
}
