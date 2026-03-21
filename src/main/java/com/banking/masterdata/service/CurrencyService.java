package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Currency;
import com.banking.masterdata.dto.request.CreateCurrencyRequest;
import com.banking.masterdata.dto.request.UpdateCurrencyRequest;
import com.banking.masterdata.dto.response.CurrencyResponse;
import com.banking.masterdata.exception.CurrencyAlreadyExistsException;
import com.banking.masterdata.exception.CurrencyNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final MasterDataMapper masterDataMapper;

    public CurrencyService(CurrencyRepository currencyRepository, MasterDataMapper masterDataMapper) {
        this.currencyRepository = currencyRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public CurrencyResponse createCurrency(CreateCurrencyRequest request) {
        if (currencyRepository.existsByCode(request.getCode())) {
            throw new CurrencyAlreadyExistsException(request.getCode());
        }

        Currency currency = masterDataMapper.toEntity(request);
        Currency savedCurrency = currencyRepository.save(currency);

        return masterDataMapper.toResponse(savedCurrency);
    }

    @Transactional(readOnly = true)
    public CurrencyResponse getCurrency(String code) {
        Currency currency = currencyRepository.findById(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));
        return masterDataMapper.toResponse(currency);
    }

    @Transactional(readOnly = true)
    public List<CurrencyResponse> getAllCurrencies(boolean activeOnly) {
        List<Currency> currencies;
        if (activeOnly) {
            currencies = currencyRepository.findByIsActiveTrue();
        } else {
            currencies = currencyRepository.findAll();
        }
        return currencies.stream()
                .map(masterDataMapper::toResponse)
                .toList();
    }

    public CurrencyResponse updateCurrency(String code, UpdateCurrencyRequest request) {
        Currency currency = currencyRepository.findById(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        if (request.getName() != null) {
            currency.setName(request.getName());
        }
        if (request.getSymbol() != null) {
            currency.setSymbol(request.getSymbol());
        }

        Currency savedCurrency = currencyRepository.save(currency);
        return masterDataMapper.toResponse(savedCurrency);
    }

    public CurrencyResponse deactivateCurrency(String code) {
        Currency currency = currencyRepository.findById(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        currency.setActive(false);
        Currency savedCurrency = currencyRepository.save(currency);
        return masterDataMapper.toResponse(savedCurrency);
    }
}
