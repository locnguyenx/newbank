package com.banking.product.dto.request;

import com.banking.product.domain.enums.CustomerType;
import com.banking.product.domain.enums.ProductFamily;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateProductRequest {

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String name;

    @NotNull(message = "Product family is required")
    private ProductFamily family;

    private String description;

    @Valid
    private List<CustomerType> customerTypes;

    public CreateProductRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductFamily getFamily() {
        return family;
    }

    public void setFamily(ProductFamily family) {
        this.family = family;
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