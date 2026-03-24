package com.banking.cashmanagement.integration;

import com.banking.customer.api.CustomerQueryService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class CustomerServiceAdapter {
    
    private final CustomerQueryService customerQueryService;
    
    public CustomerServiceAdapter(CustomerQueryService customerQueryService) {
        this.customerQueryService = customerQueryService;
    }
    
    public boolean isValidCustomer(Long customerId) {
        return customerQueryService.existsById(customerId);
    }
    
    public String getCustomerName(Long customerId) {
        return customerQueryService.getCustomerName(customerId);
    }
    
    public Optional<String> getCustomerNameOpt(Long customerId) {
        String name = customerQueryService.getCustomerName(customerId);
        return Optional.ofNullable(name);
    }
}
