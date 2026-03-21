package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Currency;
import com.banking.masterdata.domain.entity.ExchangeRate;
import com.banking.masterdata.dto.request.CreateExchangeRateRequest;
import com.banking.masterdata.dto.response.ExchangeRateResponse;
import com.banking.masterdata.exception.CurrencyNotFoundException;
import com.banking.masterdata.exception.ExchangeRateNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.CurrencyRepository;
import com.banking.masterdata.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService(exchangeRateRepository, currencyRepository, masterDataMapper);
    }

    private CreateExchangeRateRequest createRequest(String baseCode, String targetCode, BigDecimal rate, LocalDate date) {
        CreateExchangeRateRequest request = new CreateExchangeRateRequest();
        request.setBaseCurrencyCode(baseCode);
        request.setTargetCurrencyCode(targetCode);
        request.setRate(rate);
        request.setEffectiveDate(date);
        return request;
    }

    @Test
    void createExchangeRate_success() {
        CreateExchangeRateRequest request = createRequest("USD", "EUR", new BigDecimal("0.920000"), LocalDate.of(2026, 3, 21));

        Currency usd = new Currency("USD", "US Dollar", "$", 2);
        Currency eur = new Currency("EUR", "Euro", "\u20ac", 2);
        ExchangeRate exchangeRate = new ExchangeRate(usd, eur, new BigDecimal("0.920000"), LocalDate.of(2026, 3, 21));

        when(currencyRepository.findById("USD")).thenReturn(Optional.of(usd));
        when(currencyRepository.findById("EUR")).thenReturn(Optional.of(eur));
        when(masterDataMapper.toEntity(any(CreateExchangeRateRequest.class), eq(currencyRepository))).thenReturn(exchangeRate);
        when(exchangeRateRepository.save(any())).thenReturn(exchangeRate);
        when(masterDataMapper.toResponse(any(ExchangeRate.class))).thenReturn(ExchangeRateResponse.fromEntity(exchangeRate));

        ExchangeRateResponse response = exchangeRateService.createExchangeRate(request);

        assertEquals("USD", response.getBaseCurrencyCode());
        assertEquals("EUR", response.getTargetCurrencyCode());
        assertEquals(0, new BigDecimal("0.920000").compareTo(response.getRate()));

        verify(exchangeRateRepository).save(any());
    }

    @Test
    void createExchangeRate_invalidBaseCurrency_throws() {
        CreateExchangeRateRequest request = createRequest("XXX", "EUR", new BigDecimal("0.920000"), LocalDate.of(2026, 3, 21));

        when(currencyRepository.findById("XXX")).thenReturn(Optional.empty());

        assertThrows(CurrencyNotFoundException.class, () ->
                exchangeRateService.createExchangeRate(request));

        verify(exchangeRateRepository, never()).save(any());
    }

    @Test
    void createExchangeRate_invalidTargetCurrency_throws() {
        CreateExchangeRateRequest request = createRequest("USD", "XXX", new BigDecimal("0.920000"), LocalDate.of(2026, 3, 21));

        Currency usd = new Currency("USD", "US Dollar", "$", 2);

        when(currencyRepository.findById("USD")).thenReturn(Optional.of(usd));
        when(currencyRepository.findById("XXX")).thenReturn(Optional.empty());

        assertThrows(CurrencyNotFoundException.class, () ->
                exchangeRateService.createExchangeRate(request));

        verify(exchangeRateRepository, never()).save(any());
    }

    @Test
    void getLatestRate_returnsMostRecent() {
        Currency usd = new Currency("USD", "US Dollar", "$", 2);
        Currency eur = new Currency("EUR", "Euro", "\u20ac", 2);
        ExchangeRate latestRate = new ExchangeRate(usd, eur, new BigDecimal("0.930000"), LocalDate.of(2026, 3, 21));

        when(exchangeRateRepository.findTopByBaseCurrencyCodeAndTargetCurrencyCodeOrderByEffectiveDateDesc("USD", "EUR"))
                .thenReturn(Optional.of(latestRate));
        when(masterDataMapper.toResponse(any(ExchangeRate.class))).thenReturn(ExchangeRateResponse.fromEntity(latestRate));

        ExchangeRateResponse response = exchangeRateService.getLatestRate("USD", "EUR");

        assertEquals("USD", response.getBaseCurrencyCode());
        assertEquals("EUR", response.getTargetCurrencyCode());
        assertEquals(0, new BigDecimal("0.930000").compareTo(response.getRate()));
    }

    @Test
    void convertAmount_correct() {
        Currency usd = new Currency("USD", "US Dollar", "$", 2);
        Currency eur = new Currency("EUR", "Euro", "\u20ac", 2);
        ExchangeRate rate = new ExchangeRate(usd, eur, new BigDecimal("0.920000"), LocalDate.of(2026, 3, 21));

        when(exchangeRateRepository.findTopByBaseCurrencyCodeAndTargetCurrencyCodeOrderByEffectiveDateDesc("USD", "EUR"))
                .thenReturn(Optional.of(rate));

        BigDecimal converted = exchangeRateService.convertAmount("USD", "EUR", new BigDecimal("100"));

        assertEquals(0, new BigDecimal("92.00").compareTo(converted));
    }

    @Test
    void convertAmount_zeroAmount() {
        Currency usd = new Currency("USD", "US Dollar", "$", 2);
        Currency eur = new Currency("EUR", "Euro", "\u20ac", 2);
        ExchangeRate rate = new ExchangeRate(usd, eur, new BigDecimal("0.920000"), LocalDate.of(2026, 3, 21));

        when(exchangeRateRepository.findTopByBaseCurrencyCodeAndTargetCurrencyCodeOrderByEffectiveDateDesc("USD", "EUR"))
                .thenReturn(Optional.of(rate));

        BigDecimal converted = exchangeRateService.convertAmount("USD", "EUR", BigDecimal.ZERO);

        assertEquals(0, BigDecimal.ZERO.compareTo(converted));
    }
}
