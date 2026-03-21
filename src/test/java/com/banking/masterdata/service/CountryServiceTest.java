package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.dto.request.CreateCountryRequest;
import com.banking.masterdata.dto.response.CountryResponse;
import com.banking.masterdata.exception.CountryNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryService(countryRepository, masterDataMapper);
    }

    private CreateCountryRequest createRequest(String isoCode, String name, String region) {
        CreateCountryRequest request = new CreateCountryRequest();
        request.setIsoCode(isoCode);
        request.setName(name);
        request.setRegion(region);
        return request;
    }

    @Test
    void createCountry_success() {
        CreateCountryRequest request = createRequest("US", "United States", "North America");
        Country country = new Country("US", "United States", "North America");

        when(countryRepository.existsById("US")).thenReturn(false);
        when(masterDataMapper.toEntity(org.mockito.ArgumentMatchers.any(CreateCountryRequest.class))).thenReturn(country);
        when(countryRepository.save(any())).thenReturn(country);

        CountryResponse expected = CountryResponse.fromEntity(country);
        when(masterDataMapper.toResponse(org.mockito.ArgumentMatchers.any(Country.class))).thenReturn(expected);

        CountryResponse response = countryService.createCountry(request);

        assertEquals("US", response.getIsoCode());
        assertEquals("United States", response.getName());
        assertEquals("North America", response.getRegion());
        assertTrue(response.isActive());

        verify(countryRepository).save(any());
    }

    @Test
    void createCountry_duplicateCode_throws() {
        CreateCountryRequest request = createRequest("US", "United States", "North America");

        when(countryRepository.existsById("US")).thenReturn(true);

        assertThrows(IllegalStateException.class, () ->
                countryService.createCountry(request));

        verify(countryRepository, never()).save(any());
    }

    @Test
    void getCountry_success() {
        Country country = new Country("US", "United States", "North America");
        CountryResponse expected = CountryResponse.fromEntity(country);

        when(countryRepository.findById("US")).thenReturn(Optional.of(country));
        when(masterDataMapper.toResponse(org.mockito.ArgumentMatchers.any(Country.class))).thenReturn(expected);

        CountryResponse response = countryService.getCountry("US");

        assertEquals("US", response.getIsoCode());
        assertEquals("United States", response.getName());
    }

    @Test
    void getCountry_notFound_throws() {
        when(countryRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () ->
                countryService.getCountry("INVALID"));
    }

    @Test
    void getAllCountries_activeOnly() {
        Country activeUs = new Country("US", "United States", "North America");
        Country activeDe = new Country("DE", "Germany", "Europe");

        when(countryRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(activeUs, activeDe));
        when(masterDataMapper.toResponse(activeUs)).thenReturn(CountryResponse.fromEntity(activeUs));
        when(masterDataMapper.toResponse(activeDe)).thenReturn(CountryResponse.fromEntity(activeDe));

        List<CountryResponse> responses = countryService.getAllCountries(true);

        assertEquals(2, responses.size());
        verify(countryRepository).findByIsActiveTrue();
        verify(countryRepository, never()).findAll();
    }

    @Test
    void getAllCountries_all_includesInactive() {
        Country activeUs = new Country("US", "United States", "North America");
        Country inactiveXx = new Country("XX", "Inactive Country", null);
        inactiveXx.setActive(false);

        when(countryRepository.findAll()).thenReturn(Arrays.asList(activeUs, inactiveXx));
        when(masterDataMapper.toResponse(activeUs)).thenReturn(CountryResponse.fromEntity(activeUs));
        when(masterDataMapper.toResponse(inactiveXx)).thenReturn(CountryResponse.fromEntity(inactiveXx));

        List<CountryResponse> responses = countryService.getAllCountries(false);

        assertEquals(2, responses.size());
        verify(countryRepository).findAll();
        verify(countryRepository, never()).findByIsActiveTrue();
    }

    @Test
    void deactivateCountry_success() {
        Country activeCountry = new Country("US", "United States", "North America");
        Country deactivatedCountry = new Country("US", "United States", "North America");
        deactivatedCountry.setActive(false);

        when(countryRepository.findById("US")).thenReturn(Optional.of(activeCountry));
        when(countryRepository.save(any())).thenReturn(deactivatedCountry);
        when(masterDataMapper.toResponse(deactivatedCountry)).thenReturn(CountryResponse.fromEntity(deactivatedCountry));

        CountryResponse response = countryService.deactivateCountry("US");

        assertEquals("US", response.getIsoCode());
        assertFalse(response.isActive());
    }

    @Test
    void deactivateCountry_notFound_throws() {
        when(countryRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () ->
                countryService.deactivateCountry("INVALID"));
    }
}
