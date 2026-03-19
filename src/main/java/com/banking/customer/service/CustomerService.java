package com.banking.customer.service;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.SMECustomer;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.CustomerType;
import com.banking.customer.dto.CreateCorporateCustomerRequest;
import com.banking.customer.dto.CreateIndividualCustomerRequest;
import com.banking.customer.dto.CreateSMECustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.dto.CustomerSearchCriteria;
import com.banking.customer.dto.UpdateCustomerRequest;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.exception.DuplicateCustomerException;
import com.banking.customer.mapper.CustomerMapper;
import com.banking.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CustomerService {

    private static final String CUSTOMER_NUMBER_PREFIX = "CUST";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Random RANDOM = new Random();
    private static final AtomicInteger counter = new AtomicInteger(100000);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponse createCorporate(CreateCorporateCustomerRequest request) {
        validateUniqueness(request.getTaxId(), null);
        String customerNumber = generateCustomerNumber();
        CorporateCustomer customer = customerMapper.toCorporateEntity(request, customerNumber);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponse createSME(CreateSMECustomerRequest request) {
        validateUniqueness(request.getTaxId(), null);
        String customerNumber = generateCustomerNumber();
        SMECustomer customer = customerMapper.toSMEEntity(request, customerNumber);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponse createIndividual(CreateIndividualCustomerRequest request) {
        validateUniqueness(request.getTaxId(), null);
        String customerNumber = generateCustomerNumber();
        IndividualCustomer customer = customerMapper.toIndividualEntity(request, customerNumber);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setName(request.getName());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByCustomerNumber(String customerNumber) {
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
            .orElseThrow(() -> new CustomerNotFoundException(customerNumber));
        return customerMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchCustomers(CustomerSearchCriteria criteria, Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(new CustomerSearchSpecification(criteria), pageable);
        return customers.map(customerMapper::toResponse);
    }

    private void validateUniqueness(String taxId, String customerNumber) {
        if (taxId != null) {
            customerRepository.findByTaxId(taxId)
                .ifPresent(c -> {
                    throw new DuplicateCustomerException("taxId", taxId);
                });
        }
        if (customerNumber != null) {
            customerRepository.findByCustomerNumber(customerNumber)
                .ifPresent(c -> {
                    throw new DuplicateCustomerException("customerNumber", customerNumber);
                });
        }
    }

    private String generateCustomerNumber() {
        String datePart = LocalDate.now().format(DATE_FORMATTER);
        int sequence = counter.getAndIncrement();
        if (sequence > 999999) {
            counter.set(100000);
            sequence = counter.getAndIncrement();
        }
        return String.format("%s-%s-%06d", CUSTOMER_NUMBER_PREFIX, datePart, sequence);
    }
}
