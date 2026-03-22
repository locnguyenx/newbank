package com.banking.masterdata.api;

import com.banking.masterdata.api.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyQueryService {

    CurrencyDTO findByCode(String code);

    List<CurrencyDTO> findAllActive();

    boolean existsByCode(String code);
}
