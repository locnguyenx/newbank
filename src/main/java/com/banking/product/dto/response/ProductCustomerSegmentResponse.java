package com.banking.product.dto.response;

import com.banking.product.domain.entity.ProductCustomerSegment;
import com.banking.customer.domain.enums.CustomerType;

public class ProductCustomerSegmentResponse {

    private Long id;
    private CustomerType customerType;

    public ProductCustomerSegmentResponse() {
    }

    public static ProductCustomerSegmentResponse fromEntity(ProductCustomerSegment segment) {
        ProductCustomerSegmentResponse response = new ProductCustomerSegmentResponse();
        response.id = segment.getId();
        response.customerType = segment.getCustomerType();
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}