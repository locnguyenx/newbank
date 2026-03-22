package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.dto.request.CreateCountryRequest;
import com.banking.masterdata.dto.request.UpdateCountryRequest;
import com.banking.masterdata.dto.response.CountryResponse;
import com.banking.masterdata.exception.CountryNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;
    private final MasterDataMapper masterDataMapper;

    public CountryService(CountryRepository countryRepository, MasterDataMapper masterDataMapper) {
        this.countryRepository = countryRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public CountryResponse createCountry(CreateCountryRequest request) {
        if (countryRepository.existsById(request.getIsoCode())) {
            throw new IllegalStateException("Country already exists: " + request.getIsoCode());
        }

        Country country = masterDataMapper.toEntity(request);
        Country savedCountry = countryRepository.save(country);

        return masterDataMapper.toResponse(savedCountry);
    }

    @Transactional(readOnly = true)
    public CountryResponse getCountry(String isoCode) {
        Country country = countryRepository.findById(isoCode)
                .orElseThrow(() -> new CountryNotFoundException(isoCode));
        return masterDataMapper.toResponse(country);
    }

    @Transactional(readOnly = true)
    public List<CountryResponse> getAllCountries(boolean activeOnly) {
        List<Country> countries;
        if (activeOnly) {
            countries = countryRepository.findByIsActiveTrue();
        } else {
            countries = countryRepository.findAll();
        }
        return countries.stream()
                .map(masterDataMapper::toResponse)
                .toList();
    }

    public CountryResponse deactivateCountry(String isoCode) {
        Country country = countryRepository.findById(isoCode)
                .orElseThrow(() -> new CountryNotFoundException(isoCode));

        country.setActive(false);
        Country savedCountry = countryRepository.save(country);
        return masterDataMapper.toResponse(savedCountry);
    }

    public CountryResponse updateCountry(String isoCode, UpdateCountryRequest request) {
        Country country = countryRepository.findById(isoCode)
                .orElseThrow(() -> new CountryNotFoundException(isoCode));

        if (request.getName() != null) {
            country.setName(request.getName());
        }
        if (request.getRegion() != null) {
            country.setRegion(request.getRegion());
        }

        Country savedCountry = countryRepository.save(country);
        return masterDataMapper.toResponse(savedCountry);
    }
}
