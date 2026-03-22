package com.banking.masterdata.api;

import com.banking.masterdata.api.dto.CurrencyDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MasterDataQueryService {

    List<CurrencyDTO> getActiveCurrencies();

    BigDecimal convertAmount(String base, String target, BigDecimal amount);

    boolean isHoliday(String countryCode, LocalDate date);
}
