package com.banking.customer.service;

import com.banking.customer.api.CustomerQueryService;
import com.banking.customer.api.dto.CustomerDTO;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerRepository customerRepository;

    public CustomerQueryServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO findById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> new CustomerDTO(customer.getId(), customer.getName()))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getCustomerName(Long id) {
        return customerRepository.findById(id)
                .map(Customer::getName)
                .orElse(null);
    }
}
