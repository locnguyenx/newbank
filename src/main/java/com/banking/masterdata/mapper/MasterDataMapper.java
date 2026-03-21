package com.banking.masterdata.mapper;

import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.domain.entity.Currency;
import com.banking.masterdata.domain.entity.ExchangeRate;
import com.banking.masterdata.domain.entity.Holiday;
import com.banking.masterdata.domain.entity.Industry;
import com.banking.masterdata.dto.request.CreateCountryRequest;
import com.banking.masterdata.dto.request.CreateCurrencyRequest;
import com.banking.masterdata.dto.request.CreateExchangeRateRequest;
import com.banking.masterdata.dto.request.CreateHolidayRequest;
import com.banking.masterdata.dto.request.CreateIndustryRequest;
import com.banking.masterdata.dto.response.CountryResponse;
import com.banking.masterdata.dto.response.CurrencyResponse;
import com.banking.masterdata.dto.response.ExchangeRateResponse;
import com.banking.masterdata.dto.response.HolidayResponse;
import com.banking.masterdata.exception.CurrencyNotFoundException;
import com.banking.masterdata.repository.CountryRepository;
import com.banking.masterdata.repository.CurrencyRepository;
import org.springframework.stereotype.Component;

@Component
public class MasterDataMapper {

    public Currency toEntity(CreateCurrencyRequest request) {
        return new Currency(
            request.getCode(),
            request.getName(),
            request.getSymbol(),
            request.getDecimalPlaces()
        );
    }

    public Industry toEntity(CreateIndustryRequest request) {
        return new Industry(
            request.getCode(),
            request.getName(),
            request.getParentCode()
        );
    }

    public Country toEntity(CreateCountryRequest request) {
        return new Country(
            request.getIsoCode(),
            request.getName(),
            request.getRegion()
        );
    }

    public CurrencyResponse toResponse(Currency currency) {
        return CurrencyResponse.fromEntity(currency);
    }

    public CountryResponse toResponse(Country country) {
        return CountryResponse.fromEntity(country);
    }

    public Holiday toEntity(CreateHolidayRequest request, CountryRepository countryRepository) {
        Country country = countryRepository.findById(request.getCountryCode())
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + request.getCountryCode()));
        return new Holiday(country, request.getHolidayDate(), request.getDescription());
    }

    public HolidayResponse toResponse(Holiday holiday) {
        return HolidayResponse.fromEntity(holiday);
    }

    public ExchangeRate toEntity(CreateExchangeRateRequest request, CurrencyRepository currencyRepository) {
        Currency baseCurrency = currencyRepository.findById(request.getBaseCurrencyCode())
                .orElseThrow(() -> new CurrencyNotFoundException(request.getBaseCurrencyCode()));
        Currency targetCurrency = currencyRepository.findById(request.getTargetCurrencyCode())
                .orElseThrow(() -> new CurrencyNotFoundException(request.getTargetCurrencyCode()));
        return new ExchangeRate(baseCurrency, targetCurrency, request.getRate(), request.getEffectiveDate());
    }

    public ExchangeRateResponse toResponse(ExchangeRate exchangeRate) {
        return ExchangeRateResponse.fromEntity(exchangeRate);
    }
}
