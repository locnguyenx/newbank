package com.banking.product.dto.request;

import com.banking.customer.domain.enums.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ProductSegmentRequest {

    @NotNull(message = "Customer types are required")
    @Valid
    private List<@NotNull CustomerType> customerTypes;

    public ProductSegmentRequest() {
    }

    public List<CustomerType> getCustomerTypes() {
        return customerTypes;
    }

    public void setCustomerTypes(List<CustomerType> customerTypes) {
        this.customerTypes = customerTypes;
    }
}