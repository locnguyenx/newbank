package com.banking.masterdata.service;

import com.banking.masterdata.api.CurrencyQueryService;
import com.banking.masterdata.api.dto.CurrencyDTO;
import com.banking.masterdata.domain.entity.Currency;
import com.banking.masterdata.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyQueryServiceImpl implements CurrencyQueryService {

    private final CurrencyRepository currencyRepository;

    public CurrencyQueryServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyDTO findByCode(String code) {
        return currencyRepository.findById(code)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDTO> findAllActive() {
        return currencyRepository.findByIsActiveTrue().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return currencyRepository.existsByCode(code);
    }

    private CurrencyDTO toDTO(Currency currency) {
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode(currency.getCode());
        dto.setName(currency.getName());
        dto.setDecimalPlaces(currency.getDecimalPlaces());
        dto.setActive(currency.isActive());
        return dto;
    }
}
