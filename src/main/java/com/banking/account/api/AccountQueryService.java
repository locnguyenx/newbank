package com.banking.account.api;

import com.banking.account.api.dto.AccountDTO;

import java.util.List;

/**
 * Public API for querying account data.
 * Other modules should depend on this interface, not on internal implementations.
 */
public interface AccountQueryService {

    /**
     * Retrieves an account by its ID.
     *
     * @param id the ID of the account to retrieve
     * @return the account DTO if found, otherwise null
     */
    AccountDTO findById(Long id);

    /**
     * Checks if an account exists by its ID.
     *
     * @param id the ID of the account to check
     * @return true if the account exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Retrieves all accounts for a given customer.
     *
     * @param customerId the ID of the customer
     * @return list of account DTOs belonging to the customer
     */
    List<AccountDTO> findByCustomerId(Long customerId);
}
