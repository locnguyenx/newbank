package com.banking.customer.mapper;

import com.banking.customer.domain.embeddable.Address;
import com.banking.customer.domain.embeddable.Money;
import com.banking.customer.domain.embeddable.PhoneNumber;
import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.SMECustomer;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.dto.CreateCorporateCustomerRequest;
import com.banking.customer.dto.CreateIndividualCustomerRequest;
import com.banking.customer.dto.CreateSMECustomerRequest;
import com.banking.customer.dto.CustomerResponse;
import com.banking.masterdata.api.CurrencyQueryService;
import com.banking.masterdata.api.dto.CurrencyDTO;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final CurrencyQueryService currencyQueryService;

    public CustomerMapper(CurrencyQueryService currencyQueryService) {
        this.currencyQueryService = currencyQueryService;
    }

    private void validateCurrency(String code) {
        if (code == null || code.isBlank()) {
            return;
        }
        CurrencyDTO currencyDTO = currencyQueryService.findByCode(code);
        if (currencyDTO == null) {
            throw new IllegalArgumentException("Invalid currency code: " + code);
        }
        if (!currencyDTO.isActive()) {
            throw new IllegalArgumentException("Currency is inactive: " + code);
        }
    }

    public CorporateCustomer toCorporateEntity(CreateCorporateCustomerRequest request, String customerNumber) {
        CorporateCustomer customer = new CorporateCustomer(customerNumber, request.getName(), CustomerStatus.PENDING);
        customer.setTaxId(request.getTaxId());
        customer.setRegistrationNumber(request.getRegistrationNumber());
        customer.setIndustry(request.getIndustry());
        if (request.getAnnualRevenueAmount() != null && request.getAnnualRevenueCurrency() != null) {
            validateCurrency(request.getAnnualRevenueCurrency());
            customer.setAnnualRevenue(new Money(
                new BigDecimal(request.getAnnualRevenueAmount()),
                request.getAnnualRevenueCurrency()
            ));
        }
        customer.setEmployeeCount(request.getEmployeeCount());
        customer.setWebsite(request.getWebsite());
        applyContactDetails(customer, request.getAddresses(), request.getPhones(), request.getEmails());
        return customer;
    }

    public SMECustomer toSMEEntity(CreateSMECustomerRequest request, String customerNumber) {
        SMECustomer customer = new SMECustomer(customerNumber, request.getName(), CustomerStatus.PENDING);
        customer.setTaxId(request.getTaxId());
        customer.setRegistrationNumber(request.getRegistrationNumber());
        customer.setIndustry(request.getIndustry());
        customer.setBusinessType(request.getBusinessType());
        if (request.getAnnualTurnoverAmount() != null && request.getAnnualTurnoverCurrency() != null) {
            validateCurrency(request.getAnnualTurnoverCurrency());
            customer.setAnnualTurnover(new Money(
                new BigDecimal(request.getAnnualTurnoverAmount()),
                request.getAnnualTurnoverCurrency()
            ));
        }
        customer.setYearsInOperation(request.getYearsInOperation());
        applyContactDetails(customer, request.getAddresses(), request.getPhones(), request.getEmails());
        return customer;
    }

    public IndividualCustomer toIndividualEntity(CreateIndividualCustomerRequest request, String customerNumber) {
        IndividualCustomer customer = new IndividualCustomer(customerNumber, request.getName(), CustomerStatus.PENDING);
        customer.setTaxId(request.getTaxId());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setPlaceOfBirth(request.getPlaceOfBirth());
        customer.setNationality(request.getNationality());
        customer.setEmployerId(request.getEmployerId());
        customer.setEmploymentStatus(request.getEmploymentStatus());
        applyContactDetails(customer, request.getAddresses(), request.getPhones(), request.getEmails());
        return customer;
    }

    private void applyContactDetails(Customer customer,
            List<CreateCorporateCustomerRequest.AddressDto> addresses,
            List<CreateCorporateCustomerRequest.PhoneDto> phones,
            List<String> emails) {
        if (addresses != null) {
            customer.getAddresses().addAll(addresses.stream()
                .map(a -> new Address(a.getStreet(), a.getCity(), a.getState(), a.getPostalCode(), a.getCountry()))
                .collect(Collectors.toList()));
        }
        if (phones != null) {
            customer.getPhones().addAll(phones.stream()
                .map(p -> new PhoneNumber(p.getCountryCode(), p.getNumber(), p.getType()))
                .collect(Collectors.toList()));
        }
        if (emails != null) {
            customer.getEmails().addAll(emails);
        }
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.fromEntity(customer);
    }
}
