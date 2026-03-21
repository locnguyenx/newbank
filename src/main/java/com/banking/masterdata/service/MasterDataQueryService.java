package com.banking.masterdata.service;

import com.banking.masterdata.dto.response.BranchResponse;
import com.banking.masterdata.dto.response.ChannelResponse;
import com.banking.masterdata.dto.response.CountryResponse;
import com.banking.masterdata.dto.response.CurrencyResponse;
import com.banking.masterdata.dto.response.DocumentTypeResponse;
import com.banking.masterdata.dto.response.IndustryResponse;
import com.banking.masterdata.repository.BranchRepository;
import com.banking.masterdata.repository.ChannelRepository;
import com.banking.masterdata.repository.CountryRepository;
import com.banking.masterdata.repository.CurrencyRepository;
import com.banking.masterdata.repository.DocumentTypeRepository;
import com.banking.masterdata.repository.IndustryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MasterDataQueryService {

    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;
    private final IndustryRepository industryRepository;
    private final ExchangeRateService exchangeRateService;
    private final HolidayService holidayService;
    private final BranchRepository branchRepository;
    private final ChannelRepository channelRepository;
    private final DocumentTypeRepository documentTypeRepository;

    public MasterDataQueryService(CurrencyRepository currencyRepository,
                                  CountryRepository countryRepository,
                                  IndustryRepository industryRepository,
                                  ExchangeRateService exchangeRateService,
                                  HolidayService holidayService,
                                  BranchRepository branchRepository,
                                  ChannelRepository channelRepository,
                                  DocumentTypeRepository documentTypeRepository) {
        this.currencyRepository = currencyRepository;
        this.countryRepository = countryRepository;
        this.industryRepository = industryRepository;
        this.exchangeRateService = exchangeRateService;
        this.holidayService = holidayService;
        this.branchRepository = branchRepository;
        this.channelRepository = channelRepository;
        this.documentTypeRepository = documentTypeRepository;
    }

    public List<CurrencyResponse> getActiveCurrencies() {
        return currencyRepository.findByIsActiveTrue().stream()
                .map(CurrencyResponse::fromEntity)
                .toList();
    }

    public List<CountryResponse> getActiveCountries() {
        return countryRepository.findByIsActiveTrue().stream()
                .map(CountryResponse::fromEntity)
                .toList();
    }

    public List<IndustryResponse> getActiveIndustries() {
        return industryRepository.findByIsActiveTrue().stream()
                .map(IndustryResponse::fromEntity)
                .toList();
    }

    public BigDecimal convertAmount(String base, String target, BigDecimal amount) {
        return exchangeRateService.convertAmount(base, target, amount);
    }

    public boolean isHoliday(String countryCode, LocalDate date) {
        return holidayService.isHoliday(countryCode, date);
    }

    public List<BranchResponse> getActiveBranches() {
        return branchRepository.findByIsActiveTrue().stream()
                .map(BranchResponse::fromEntity)
                .toList();
    }

    public List<ChannelResponse> getActiveChannels() {
        return channelRepository.findByIsActiveTrue().stream()
                .map(ChannelResponse::fromEntity)
                .toList();
    }

    public List<DocumentTypeResponse> getActiveDocumentTypes(String category) {
        if (category == null) {
            return documentTypeRepository.findByIsActiveTrue().stream()
                    .map(DocumentTypeResponse::fromEntity)
                    .toList();
        }
        return documentTypeRepository.findByIsActiveTrueAndCategory(category).stream()
                .map(DocumentTypeResponse::fromEntity)
                .toList();
    }
}
