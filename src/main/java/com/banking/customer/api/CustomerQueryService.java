package com.banking.customer.api;

import com.banking.customer.api.dto.CustomerDTO;

/**
 * Service for querying customer data.
 * This is the public API for the customer module that other modules can depend on.
 */
public interface CustomerQueryService {

    /**
     * Retrieves a customer by their ID.
     * 
     * @param customerId the ID of the customer to retrieve
     * @return the customer DTO if found, otherwise null
     */
    CustomerDTO findById(Long id);

    /**
     * Checks if a customer exists by their ID.
     * 
     * @param customerId the ID of the customer to check
     * @return true if the customer exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Retrieves the customer's name by their ID.
     * 
     * @param customerId the ID of the customer
     * @return the customer's name if found, otherwise null
     */
    String getCustomerName(Long id);
}