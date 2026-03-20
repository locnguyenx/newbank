package com.banking.product.dto.request;

import com.banking.customer.domain.enums.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;

public class UpdateProductRequest {

    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String name;

    private String description;

    @Valid
    private List<CustomerType> customerTypes;

    public UpdateProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CustomerType> getCustomerTypes() {
        return customerTypes;
    }

    public void setCustomerTypes(List<CustomerType> customerTypes) {
        this.customerTypes = customerTypes;
    }
}