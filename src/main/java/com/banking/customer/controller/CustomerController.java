package com.banking.customer.controller;

import com.banking.customer.domain.enums.CustomerType;
import com.banking.customer.dto.CreateCorporateCustomerRequest;
import com.banking.customer.dto.CreateIndividualCustomerRequest;
import com.banking.customer.dto.CreateSMECustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.dto.CustomerSearchCriteria;
import com.banking.customer.dto.UpdateCustomerRequest;
import com.banking.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/corporate")
    public ResponseEntity<CustomerResponse> createCorporate(@Valid @RequestBody CreateCorporateCustomerRequest request) {
        CustomerResponse response = customerService.createCorporate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sme")
    public ResponseEntity<CustomerResponse> createSME(@Valid @RequestBody CreateSMECustomerRequest request) {
        CustomerResponse response = customerService.createSME(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/individual")
    public ResponseEntity<CustomerResponse> createIndividual(@Valid @RequestBody CreateIndividualCustomerRequest request) {
        CustomerResponse response = customerService.createIndividual(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResponse>> searchCustomers(
            @ModelAttribute CustomerSearchCriteria criteria,
            Pageable pageable) {
        Page<CustomerResponse> response = customerService.searchCustomers(criteria, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }
}
