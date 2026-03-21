package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.domain.entity.Holiday;
import com.banking.masterdata.dto.request.CreateHolidayRequest;
import com.banking.masterdata.dto.response.HolidayResponse;
import com.banking.masterdata.exception.HolidayNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.CountryRepository;
import com.banking.masterdata.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;
    private final MasterDataMapper masterDataMapper;

    public HolidayService(HolidayRepository holidayRepository, CountryRepository countryRepository, MasterDataMapper masterDataMapper) {
        this.holidayRepository = holidayRepository;
        this.countryRepository = countryRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public HolidayResponse createHoliday(CreateHolidayRequest request) {
        Country country = countryRepository.findById(request.getCountryCode())
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + request.getCountryCode()));

        Holiday holiday = new Holiday(country, request.getHolidayDate(), request.getDescription());
        Holiday savedHoliday = holidayRepository.save(holiday);

        return masterDataMapper.toResponse(savedHoliday);
    }

    @Transactional(readOnly = true)
    public boolean isHoliday(String countryCode, LocalDate date) {
        return holidayRepository.existsByCountryIsoCodeAndHolidayDate(countryCode, date);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> getHolidays(String countryCode, int year) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);

        List<Holiday> holidays = holidayRepository.findByCountryIsoCodeAndHolidayDateBetween(countryCode, yearStart, yearEnd);
        return holidays.stream()
                .map(masterDataMapper::toResponse)
                .toList();
    }
}
