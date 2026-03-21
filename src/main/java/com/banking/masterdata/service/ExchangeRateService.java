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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final MasterDataMapper masterDataMapper;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository,
                               CurrencyRepository currencyRepository,
                               MasterDataMapper masterDataMapper) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public ExchangeRateResponse createExchangeRate(CreateExchangeRateRequest request) {
        currencyRepository.findById(request.getBaseCurrencyCode())
                .orElseThrow(() -> new CurrencyNotFoundException(request.getBaseCurrencyCode()));
        currencyRepository.findById(request.getTargetCurrencyCode())
                .orElseThrow(() -> new CurrencyNotFoundException(request.getTargetCurrencyCode()));

        ExchangeRate exchangeRate = masterDataMapper.toEntity(request, currencyRepository);
        ExchangeRate saved = exchangeRateRepository.save(exchangeRate);

        return masterDataMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ExchangeRateResponse getLatestRate(String base, String target) {
        ExchangeRate rate = exchangeRateRepository
                .findTopByBaseCurrencyCodeAndTargetCurrencyCodeOrderByEffectiveDateDesc(base, target)
                .orElseThrow(() -> new ExchangeRateNotFoundException(base, target));
        return masterDataMapper.toResponse(rate);
    }

    @Transactional(readOnly = true)
    public BigDecimal convertAmount(String base, String target, BigDecimal amount) {
        ExchangeRate rate = exchangeRateRepository
                .findTopByBaseCurrencyCodeAndTargetCurrencyCodeOrderByEffectiveDateDesc(base, target)
                .orElseThrow(() -> new ExchangeRateNotFoundException(base, target));
        return amount.multiply(rate.getRate()).setScale(2, RoundingMode.HALF_UP);
    }
}
