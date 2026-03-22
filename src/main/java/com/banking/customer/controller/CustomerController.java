package com.banking.customer.controller;

import com.banking.customer.domain.enums.CustomerType;
import com.banking.customer.dto.CreateCorporateCustomerRequest;
import com.banking.customer.dto.CreateIndividualCustomerRequest;
import com.banking.customer.dto.CreateSMECustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.customer.dto.CustomerSearchCriteria;
import com.banking.customer.dto.UpdateCustomerRequest;
import com.banking.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Create a new corporate customer", description = "Creates a new corporate customer with registration details, industry, and annual revenue")
    @PostMapping("/corporate")
    public ResponseEntity<CustomerResponse> createCorporate(@Valid @RequestBody CreateCorporateCustomerRequest request) {
        CustomerResponse response = customerService.createCorporate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Create a new SME customer", description = "Creates a new small/medium enterprise customer with business type and turnover")
    @PostMapping("/sme")
    public ResponseEntity<CustomerResponse> createSME(@Valid @RequestBody CreateSMECustomerRequest request) {
        CustomerResponse response = customerService.createSME(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Create a new individual customer", description = "Creates a new individual customer with personal details")
    @PostMapping("/individual")
    public ResponseEntity<CustomerResponse> createIndividual(@Valid @RequestBody CreateIndividualCustomerRequest request) {
        CustomerResponse response = customerService.createIndividual(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve customer details by internal database ID")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(in = ParameterIn.PATH, description = "Customer database ID", required = true) @PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all customers", description = "Returns paginated list of all customers (no filtering)")
    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> listCustomers(Pageable pageable) {
        Page<CustomerResponse> response = customerService.searchCustomers(new CustomerSearchCriteria(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search customers", description = "Search customers by criteria (name, type, status, etc.)")
    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResponse>> searchCustomers(
            @ModelAttribute CustomerSearchCriteria criteria,
            Pageable pageable) {
        Page<CustomerResponse> response = customerService.searchCustomers(criteria, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update customer", description = "Update customer details. Supports partial updates.")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(in = ParameterIn.PATH, description = "Customer database ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }
}
