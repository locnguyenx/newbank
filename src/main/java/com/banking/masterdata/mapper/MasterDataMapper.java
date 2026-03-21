package com.banking.masterdata.mapper;

import com.banking.masterdata.domain.entity.Currency;
import com.banking.masterdata.dto.request.CreateCurrencyRequest;
import com.banking.masterdata.dto.response.CurrencyResponse;
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

    public CurrencyResponse toResponse(Currency currency) {
        return CurrencyResponse.fromEntity(currency);
    }
}
