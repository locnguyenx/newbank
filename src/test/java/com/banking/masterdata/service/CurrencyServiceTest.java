package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Currency;
import com.banking.masterdata.dto.request.CreateCurrencyRequest;
import com.banking.masterdata.dto.request.UpdateCurrencyRequest;
import com.banking.masterdata.dto.response.CurrencyResponse;
import com.banking.masterdata.exception.CurrencyAlreadyExistsException;
import com.banking.masterdata.exception.CurrencyNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(currencyRepository, masterDataMapper);
    }

    private CreateCurrencyRequest createRequest(String code, String name, String symbol, int decimalPlaces) {
        CreateCurrencyRequest request = new CreateCurrencyRequest();
        request.setCode(code);
        request.setName(name);
        request.setSymbol(symbol);
        request.setDecimalPlaces(decimalPlaces);
        return request;
    }

    @Test
    void createCurrency_success() {
        CreateCurrencyRequest request = createRequest("USD", "US Dollar", "$", 2);
        Currency currency = new Currency("USD", "US Dollar", "$", 2);

        when(currencyRepository.existsByCode("USD")).thenReturn(false);
        when(masterDataMapper.toEntity(any())).thenReturn(currency);
        when(currencyRepository.save(any())).thenReturn(currency);

        CurrencyResponse expected = CurrencyResponse.fromEntity(currency);
        when(masterDataMapper.toResponse(any())).thenReturn(expected);

        CurrencyResponse response = currencyService.createCurrency(request);

        assertEquals("USD", response.getCode());
        assertEquals("US Dollar", response.getName());
        assertEquals("$", response.getSymbol());
        assertEquals(2, response.getDecimalPlaces());
        assertTrue(response.isActive());

        verify(currencyRepository).save(any());
    }

    @Test
    void createCurrency_duplicateCode_throws() {
        CreateCurrencyRequest request = createRequest("USD", "US Dollar", "$", 2);

        when(currencyRepository.existsByCode("USD")).thenReturn(true);

        assertThrows(CurrencyAlreadyExistsException.class, () ->
                currencyService.createCurrency(request));

        verify(currencyRepository, never()).save(any());
    }

    @Test
    void getCurrency_success() {
        Currency currency = new Currency("USD", "US Dollar", "$", 2);
        CurrencyResponse expected = CurrencyResponse.fromEntity(currency);

        when(currencyRepository.findById("USD")).thenReturn(Optional.of(currency));
        when(masterDataMapper.toResponse(currency)).thenReturn(expected);

        CurrencyResponse response = currencyService.getCurrency("USD");

        assertEquals("USD", response.getCode());
        assertEquals("US Dollar", response.getName());
    }

    @Test
    void getCurrency_notFound_throws() {
        when(currencyRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(CurrencyNotFoundException.class, () ->
                currencyService.getCurrency("INVALID"));
    }

    @Test
    void getAllCurrencies_activeOnly_excludesInactive() {
        Currency activeUsd = new Currency("USD", "US Dollar", "$", 2);
        Currency activeEur = new Currency("EUR", "Euro", "€", 2);

        when(currencyRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(activeUsd, activeEur));
        when(masterDataMapper.toResponse(activeUsd)).thenReturn(CurrencyResponse.fromEntity(activeUsd));
        when(masterDataMapper.toResponse(activeEur)).thenReturn(CurrencyResponse.fromEntity(activeEur));

        List<CurrencyResponse> responses = currencyService.getAllCurrencies(true);

        assertEquals(2, responses.size());
        verify(currencyRepository).findByIsActiveTrue();
        verify(currencyRepository, never()).findAll();
    }

    @Test
    void getAllCurrencies_all_includesInactive() {
        Currency activeUsd = new Currency("USD", "US Dollar", "$", 2);
        Currency inactiveXxx = new Currency("XXX", "Inactive", "X", 2);
        inactiveXxx.setActive(false);

        when(currencyRepository.findAll()).thenReturn(Arrays.asList(activeUsd, inactiveXxx));
        when(masterDataMapper.toResponse(activeUsd)).thenReturn(CurrencyResponse.fromEntity(activeUsd));
        when(masterDataMapper.toResponse(inactiveXxx)).thenReturn(CurrencyResponse.fromEntity(inactiveXxx));

        List<CurrencyResponse> responses = currencyService.getAllCurrencies(false);

        assertEquals(2, responses.size());
        verify(currencyRepository).findAll();
        verify(currencyRepository, never()).findByIsActiveTrue();
    }

    @Test
    void updateCurrency_success() {
        Currency existingCurrency = new Currency("USD", "US Dollar", "$", 2);
        Currency updatedCurrency = new Currency("USD", "United States Dollar", "US$", 2);

        when(currencyRepository.findById("USD")).thenReturn(Optional.of(existingCurrency));
        when(currencyRepository.save(any())).thenReturn(updatedCurrency);
        when(masterDataMapper.toResponse(updatedCurrency)).thenReturn(CurrencyResponse.fromEntity(updatedCurrency));

        UpdateCurrencyRequest request = new UpdateCurrencyRequest();
        request.setName("United States Dollar");
        request.setSymbol("US$");

        CurrencyResponse response = currencyService.updateCurrency("USD", request);

        assertEquals("USD", response.getCode());
        assertEquals("United States Dollar", response.getName());
        assertEquals("US$", response.getSymbol());
    }

    @Test
    void updateCurrency_notFound_throws() {
        when(currencyRepository.findById("INVALID")).thenReturn(Optional.empty());

        UpdateCurrencyRequest request = new UpdateCurrencyRequest();
        request.setName("New Name");

        assertThrows(CurrencyNotFoundException.class, () ->
                currencyService.updateCurrency("INVALID", request));
    }

    @Test
    void deactivateCurrency_success() {
        Currency activeCurrency = new Currency("USD", "US Dollar", "$", 2);
        Currency deactivatedCurrency = new Currency("USD", "US Dollar", "$", 2);
        deactivatedCurrency.setActive(false);

        when(currencyRepository.findById("USD")).thenReturn(Optional.of(activeCurrency));
        when(currencyRepository.save(any())).thenReturn(deactivatedCurrency);
        when(masterDataMapper.toResponse(deactivatedCurrency)).thenReturn(CurrencyResponse.fromEntity(deactivatedCurrency));

        CurrencyResponse response = currencyService.deactivateCurrency("USD");

        assertEquals("USD", response.getCode());
        assertFalse(response.isActive());
    }

    @Test
    void deactivateCurrency_notFound_throws() {
        when(currencyRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(CurrencyNotFoundException.class, () ->
                currencyService.deactivateCurrency("INVALID"));
    }
}
