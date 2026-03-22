package com.banking.customer.service;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.SMECustomer;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.CustomerType;
import com.banking.customer.domain.enums.EmploymentStatus;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, customerMapper);
    }

    @Test
    void shouldCreateCorporateCustomer() {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        CorporateCustomer savedCustomer = createCorporateCustomer();
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findByTaxId(request.getTaxId())).thenReturn(Optional.empty());
        when(customerMapper.toCorporateEntity(any(), any())).thenReturn(savedCustomer);
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(customerMapper.toResponse(savedCustomer)).thenReturn(expectedResponse);

        CustomerResponse response = customerService.createCorporate(request);

        assertNotNull(response);
        assertEquals(CustomerType.CORPORATE, response.getType());
        verify(customerRepository).save(any(CorporateCustomer.class));
    }

    @Test
    void shouldCreateSMECustomer() {
        CreateSMECustomerRequest request = createSMERequest();
        SMECustomer savedCustomer = createSMECustomer();
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.SME);

        when(customerRepository.findByTaxId(request.getTaxId())).thenReturn(Optional.empty());
        when(customerMapper.toSMEEntity(any(), any())).thenReturn(savedCustomer);
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(customerMapper.toResponse(savedCustomer)).thenReturn(expectedResponse);

        CustomerResponse response = customerService.createSME(request);

        assertNotNull(response);
        assertEquals(CustomerType.SME, response.getType());
        verify(customerRepository).save(any(SMECustomer.class));
    }

    @Test
    void shouldCreateIndividualCustomer() {
        CreateIndividualCustomerRequest request = createIndividualRequest();
        IndividualCustomer savedCustomer = createIndividualCustomer();
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.INDIVIDUAL);

        when(customerRepository.findByTaxId(request.getTaxId())).thenReturn(Optional.empty());
        when(customerMapper.toIndividualEntity(any(), any())).thenReturn(savedCustomer);
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(customerMapper.toResponse(savedCustomer)).thenReturn(expectedResponse);

        CustomerResponse response = customerService.createIndividual(request);

        assertNotNull(response);
        assertEquals(CustomerType.INDIVIDUAL, response.getType());
        verify(customerRepository).save(any(IndividualCustomer.class));
    }

    @Test
    void shouldThrowDuplicateCustomerExceptionWhenTaxIdExists() {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        Customer existingCustomer = createCorporateCustomer();

        when(customerRepository.findByTaxId(request.getTaxId())).thenReturn(Optional.of(existingCustomer));

        assertThrows(DuplicateCustomerException.class, () -> customerService.createCorporate(request));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldUpdateCustomer() {
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");
        CorporateCustomer existingCustomer = createCorporateCustomer();
        CorporateCustomer updatedCustomer = createCorporateCustomer();
        updatedCustomer.setName("Updated Name");
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);
        expectedResponse.setName("Updated Name");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any())).thenReturn(updatedCustomer);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        CustomerResponse response = customerService.updateCustomer(customerId, request);

        assertNotNull(response);
        assertEquals("Updated Name", response.getName());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldUpdateCustomerWithAllFields() {
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("New Corporate Name");
        request.setTaxId("NEW-TAX-123");
        request.setRegistrationNumber("NEW-REG-456");
        request.setIndustry("Technology");
        request.setWebsite("https://newwebsite.com");
        request.setEmployeeCount(500);
        request.setAnnualRevenueAmount(5000000L);
        request.setAnnualRevenueCurrency("USD");
        request.setEmail("corp@newcompany.com");
        request.setPhoneNumber("+1-555-9999");

        CorporateCustomer existingCustomer = createCorporateCustomer();
        existingCustomer.setIndustry("Old Industry");
        existingCustomer.setEmployeeCount(100);
        existingCustomer.setWebsite("https://oldwebsite.com");

        ArgumentCaptor<CorporateCustomer> customerCaptor = ArgumentCaptor.forClass(CorporateCustomer.class);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findByTaxId("NEW-TAX-123")).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerMapper.toResponse(any(CorporateCustomer.class))).thenAnswer(invocation -> {
            CorporateCustomer saved = invocation.getArgument(0);
            CustomerResponse response = createCustomerResponse(CustomerType.CORPORATE);
            response.setName(saved.getName());
            response.setTaxId(saved.getTaxId());
            response.setRegistrationNumber(saved.getRegistrationNumber());
            response.setIndustry(saved.getIndustry());
            response.setWebsite(saved.getWebsite());
            response.setEmployeeCount(saved.getEmployeeCount());
            return response;
        });

        CustomerResponse response = customerService.updateCustomer(customerId, request);

        assertNotNull(response);
        assertEquals("New Corporate Name", response.getName());
        assertEquals("NEW-TAX-123", response.getTaxId());
        assertEquals("NEW-REG-456", response.getRegistrationNumber());
        assertEquals("Technology", response.getIndustry());
        assertEquals("https://newwebsite.com", response.getWebsite());
        assertEquals(500, response.getEmployeeCount());

        verify(customerRepository).save(customerCaptor.capture());
        CorporateCustomer savedCustomer = customerCaptor.getValue();
        assertEquals("New Corporate Name", savedCustomer.getName());
        assertEquals("Technology", savedCustomer.getIndustry());
        assertEquals("https://newwebsite.com", savedCustomer.getWebsite());
        assertEquals(500, savedCustomer.getEmployeeCount());
    }

    @Test
    void shouldUpdateSMECustomerWithAllFields() {
        Long customerId = 2L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("New SME Name");
        request.setTaxId("NEW-SME-TAX");
        request.setRegistrationNumber("NEW-SME-REG");
        request.setIndustry("Retail");
        request.setBusinessType("E-commerce");
        request.setAnnualTurnoverAmount(1000000L);
        request.setAnnualTurnoverCurrency("EUR");
        request.setYearsInOperation(10);

        SMECustomer existingCustomer = createSMECustomer();
        existingCustomer.setIndustry("Manufacturing");
        existingCustomer.setBusinessType("Factory");
        existingCustomer.setYearsInOperation(5);

        ArgumentCaptor<SMECustomer> customerCaptor = ArgumentCaptor.forClass(SMECustomer.class);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findByTaxId("NEW-SME-TAX")).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerMapper.toResponse(any(SMECustomer.class))).thenAnswer(invocation -> {
            SMECustomer saved = invocation.getArgument(0);
            CustomerResponse response = createCustomerResponse(CustomerType.SME);
            response.setName(saved.getName());
            response.setTaxId(saved.getTaxId());
            response.setRegistrationNumber(saved.getRegistrationNumber());
            response.setIndustry(saved.getIndustry());
            response.setBusinessType(saved.getBusinessType());
            response.setYearsInOperation(saved.getYearsInOperation());
            return response;
        });

        CustomerResponse response = customerService.updateCustomer(customerId, request);

        assertNotNull(response);
        assertEquals("New SME Name", response.getName());
        assertEquals("NEW-SME-TAX", response.getTaxId());
        assertEquals("E-commerce", response.getBusinessType());
        assertEquals(10, response.getYearsInOperation());

        verify(customerRepository).save(customerCaptor.capture());
        SMECustomer savedCustomer = customerCaptor.getValue();
        assertEquals("New SME Name", savedCustomer.getName());
        assertEquals("Retail", savedCustomer.getIndustry());
        assertEquals("E-commerce", savedCustomer.getBusinessType());
        assertEquals(10, savedCustomer.getYearsInOperation());
    }

    @Test
    void shouldUpdateIndividualCustomerWithAllFields() {
        Long customerId = 3L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("New Individual Name");
        request.setTaxId("NEW-IND-TAX");
        request.setDateOfBirth(LocalDate.of(1985, 6, 15));
        request.setPlaceOfBirth("New City");
        request.setNationality("UK");
        request.setEmploymentStatus("EMPLOYED");

        IndividualCustomer existingCustomer = createIndividualCustomer();
        existingCustomer.setPlaceOfBirth("Old City");
        existingCustomer.setNationality("US");

        ArgumentCaptor<IndividualCustomer> customerCaptor = ArgumentCaptor.forClass(IndividualCustomer.class);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findByTaxId("NEW-IND-TAX")).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerMapper.toResponse(any(IndividualCustomer.class))).thenAnswer(invocation -> {
            IndividualCustomer saved = invocation.getArgument(0);
            CustomerResponse response = createCustomerResponse(CustomerType.INDIVIDUAL);
            response.setName(saved.getName());
            response.setTaxId(saved.getTaxId());
            response.setDateOfBirth(saved.getDateOfBirth());
            response.setPlaceOfBirth(saved.getPlaceOfBirth());
            response.setNationality(saved.getNationality());
            response.setEmploymentStatus(saved.getEmploymentStatus());
            return response;
        });

        CustomerResponse response = customerService.updateCustomer(customerId, request);

        assertNotNull(response);
        assertEquals("New Individual Name", response.getName());
        assertEquals("NEW-IND-TAX", response.getTaxId());
        assertEquals(LocalDate.of(1985, 6, 15), response.getDateOfBirth());
        assertEquals("New City", response.getPlaceOfBirth());
        assertEquals("UK", response.getNationality());

        verify(customerRepository).save(customerCaptor.capture());
        IndividualCustomer savedCustomer = customerCaptor.getValue();
        assertEquals("New Individual Name", savedCustomer.getName());
        assertEquals(LocalDate.of(1985, 6, 15), savedCustomer.getDateOfBirth());
        assertEquals("New City", savedCustomer.getPlaceOfBirth());
        assertEquals("UK", savedCustomer.getNationality());
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenUpdatingNonExistentCustomer() {
        Long customerId = 999L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(customerId, request));
    }

    @Test
    void shouldGetCustomerById() {
        Long customerId = 1L;
        CorporateCustomer customer = createCorporateCustomer();
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponse(customer)).thenReturn(expectedResponse);

        CustomerResponse response = customerService.getCustomerById(customerId);

        assertNotNull(response);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenGettingNonExistentCustomerById() {
        Long customerId = 999L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    void shouldGetCustomerByCustomerNumber() {
        String customerNumber = "CUST-20240101-100000";
        Customer customer = createCorporateCustomer();
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponse(customer)).thenReturn(expectedResponse);

        CustomerResponse response = customerService.getCustomerByCustomerNumber(customerNumber);

        assertNotNull(response);
        assertEquals(customerNumber, response.getCustomerNumber());
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenGettingNonExistentCustomerByNumber() {
        String customerNumber = "CUST-INVALID";

        when(customerRepository.findByCustomerNumber(customerNumber)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByCustomerNumber(customerNumber));
    }

    @Test
    void shouldSearchCustomers() {
        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setName("Test");
        Pageable pageable = PageRequest.of(0, 10);
        CorporateCustomer customer = createCorporateCustomer();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(customerPage);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        Page<CustomerResponse> response = customerService.searchCustomers(criteria, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        verify(customerRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldGenerateUniqueCustomerNumber() {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        CorporateCustomer savedCustomer = createCorporateCustomer();
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findByTaxId(any())).thenReturn(Optional.empty());
        when(customerMapper.toCorporateEntity(any(), any())).thenReturn(savedCustomer);
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        customerService.createCorporate(request);

        ArgumentCaptor<String> customerNumberCaptor = ArgumentCaptor.forClass(String.class);
        verify(customerMapper).toCorporateEntity(any(), customerNumberCaptor.capture());
        String generatedNumber = customerNumberCaptor.getValue();
        assertTrue(generatedNumber.startsWith("CUST-"));
    }

    @Test
    void shouldHaveCorrectErrorCodeForDuplicateCustomerException() {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        Customer existingCustomer = createCorporateCustomer();

        when(customerRepository.findByTaxId(request.getTaxId())).thenReturn(Optional.of(existingCustomer));

        try {
            customerService.createCorporate(request);
            fail("Expected DuplicateCustomerException");
        } catch (DuplicateCustomerException e) {
            assertEquals("CUSTOMER_002", e.getMessageCode());
            assertTrue(e.getMessage().contains("taxId"));
        }
    }

    @Test
    void shouldHaveCorrectErrorCodeForCustomerNotFoundException() {
        Long customerId = 999L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        try {
            customerService.getCustomerById(customerId);
            fail("Expected CustomerNotFoundException");
        } catch (CustomerNotFoundException e) {
            assertEquals("CUSTOMER_001", e.getMessageCode());
        }
    }

    @Test
    void shouldSearchCustomersByStatus() {
        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setStatus(CustomerStatus.ACTIVE);
        Pageable pageable = PageRequest.of(0, 10);
        CorporateCustomer customer = createCorporateCustomer();
        customer.setStatus(CustomerStatus.ACTIVE);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(customerPage);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        Page<CustomerResponse> response = customerService.searchCustomers(criteria, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void shouldSearchCustomersByType() {
        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setType(CustomerType.CORPORATE);
        Pageable pageable = PageRequest.of(0, 10);
        CorporateCustomer customer = createCorporateCustomer();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(customerPage);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        Page<CustomerResponse> response = customerService.searchCustomers(criteria, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void shouldSearchCustomersWithMultipleFilters() {
        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setName("Test");
        criteria.setStatus(CustomerStatus.PENDING);
        criteria.setType(CustomerType.CORPORATE);
        Pageable pageable = PageRequest.of(0, 10);
        CorporateCustomer customer = createCorporateCustomer();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, customers.size());
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(customerPage);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        Page<CustomerResponse> response = customerService.searchCustomers(criteria, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void shouldUpdateCustomerWithTaxIdChange() {
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");
        request.setTaxId("NEW-TAX-ID");
        
        CorporateCustomer existingCustomer = createCorporateCustomer();
        existingCustomer.setTaxId("OLD-TAX-ID");
        
        CorporateCustomer updatedCustomer = createCorporateCustomer();
        updatedCustomer.setName("Updated Name");
        updatedCustomer.setTaxId("NEW-TAX-ID");
        
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);
        expectedResponse.setName("Updated Name");
        expectedResponse.setTaxId("NEW-TAX-ID");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findByTaxId("NEW-TAX-ID")).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(updatedCustomer);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        CustomerResponse response = customerService.updateCustomer(customerId, request);

        assertNotNull(response);
        assertEquals("Updated Name", response.getName());
        assertEquals("NEW-TAX-ID", response.getTaxId());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldThrowDuplicateCustomerExceptionWhenUpdatingTaxIdToExisting() {
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");
        request.setTaxId("EXISTING-TAX-ID");
        
        CorporateCustomer existingCustomer = createCorporateCustomer();
        existingCustomer.setTaxId("OLD-TAX-ID");
        
        CorporateCustomer otherCustomer = new CorporateCustomer("CUST-20240101-999999", "Other Corp", CustomerStatus.PENDING);
        otherCustomer.setId(2L);
        otherCustomer.setTaxId("EXISTING-TAX-ID");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findByTaxId("EXISTING-TAX-ID")).thenReturn(Optional.of(otherCustomer));

        assertThrows(DuplicateCustomerException.class, () -> customerService.updateCustomer(customerId, request));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldNotValidateTaxIdWhenNotChanged() {
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setName("Updated Name");
        request.setTaxId("12345678");
        
        CorporateCustomer existingCustomer = createCorporateCustomer();
        existingCustomer.setTaxId("12345678");
        
        CorporateCustomer updatedCustomer = createCorporateCustomer();
        updatedCustomer.setName("Updated Name");
        
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);
        expectedResponse.setName("Updated Name");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any())).thenReturn(updatedCustomer);
        when(customerMapper.toResponse(any())).thenReturn(expectedResponse);

        CustomerResponse response = customerService.updateCustomer(customerId, request);

        assertNotNull(response);
        verify(customerRepository, never()).findByTaxId(any());
    }

    @Test
    void shouldSearchCustomersReturnsEmptyPage() {
        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setName("NonExistent");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        Page<CustomerResponse> response = customerService.searchCustomers(criteria, pageable);

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
    }

    @Test
    void shouldValidateCustomerNumberUniqueness() {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        Customer existingCustomer = createCorporateCustomer();

        when(customerRepository.findByTaxId(request.getTaxId())).thenReturn(Optional.of(existingCustomer));

        assertThrows(DuplicateCustomerException.class, () -> customerService.createCorporate(request));
    }

    @Test
    void shouldHandleNullTaxIdInValidation() {
        CreateCorporateCustomerRequest request = createCorporateRequest();
        request.setTaxId(null);
        CorporateCustomer savedCustomer = createCorporateCustomer();
        CustomerResponse expectedResponse = createCustomerResponse(CustomerType.CORPORATE);

        when(customerMapper.toCorporateEntity(any(), any())).thenReturn(savedCustomer);
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(customerMapper.toResponse(savedCustomer)).thenReturn(expectedResponse);

        CustomerResponse response = customerService.createCorporate(request);

        assertNotNull(response);
        verify(customerRepository, never()).findByTaxId(any());
    }

    private CreateCorporateCustomerRequest createCorporateRequest() {
        CreateCorporateCustomerRequest request = new CreateCorporateCustomerRequest();
        request.setName("Test Corp");
        request.setTaxId("12345678");
        request.setRegistrationNumber("REG001");
        request.setIndustry("Technology");
        request.setAnnualRevenueAmount("1000000");
        request.setAnnualRevenueCurrency("USD");
        request.setEmployeeCount(100);
        request.setWebsite("https://test.com");
        return request;
    }

    private CreateSMECustomerRequest createSMERequest() {
        CreateSMECustomerRequest request = new CreateSMECustomerRequest();
        request.setName("Test SME");
        request.setTaxId("87654321");
        request.setRegistrationNumber("SME001");
        request.setIndustry("Retail");
        request.setBusinessType("Retail Store");
        request.setAnnualTurnoverAmount("500000");
        request.setAnnualTurnoverCurrency("USD");
        request.setYearsInOperation(5);
        return request;
    }

    private CreateIndividualCustomerRequest createIndividualRequest() {
        CreateIndividualCustomerRequest request = new CreateIndividualCustomerRequest();
        request.setName("John Doe");
        request.setTaxId("11122233");
        request.setDateOfBirth(LocalDate.of(1990, 1, 15));
        request.setPlaceOfBirth("New York");
        request.setNationality("US");
        request.setEmploymentStatus(EmploymentStatus.ACTIVE);
        return request;
    }

    private CorporateCustomer createCorporateCustomer() {
        CorporateCustomer customer = new CorporateCustomer("CUST-20240101-100000", "Test Corp", CustomerStatus.PENDING);
        customer.setTaxId("12345678");
        return customer;
    }

    private SMECustomer createSMECustomer() {
        SMECustomer customer = new SMECustomer("CUST-20240101-100001", "Test SME", CustomerStatus.PENDING);
        customer.setTaxId("87654321");
        return customer;
    }

    private IndividualCustomer createIndividualCustomer() {
        IndividualCustomer customer = new IndividualCustomer("CUST-20240101-100002", "John Doe", CustomerStatus.PENDING);
        customer.setTaxId("11122233");
        return customer;
    }

    private CustomerResponse createCustomerResponse(CustomerType type) {
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);
        response.setCustomerNumber("CUST-20240101-100000");
        response.setType(type);
        response.setStatus(CustomerStatus.PENDING);
        response.setName("Test Customer");
        response.setTaxId("12345678");
        return response;
    }
}
