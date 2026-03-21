package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateCountryRequest;
import com.banking.masterdata.dto.response.CountryResponse;
import com.banking.masterdata.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-data/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CreateCountryRequest request) {
        CountryResponse response = countryService.createCountry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAllCountries(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<CountryResponse> response = countryService.getAllCountries(activeOnly);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{isoCode}")
    public ResponseEntity<CountryResponse> getCountry(@PathVariable String isoCode) {
        CountryResponse response = countryService.getCountry(isoCode);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{isoCode}/deactivate")
    public ResponseEntity<CountryResponse> deactivateCountry(@PathVariable String isoCode) {
        CountryResponse response = countryService.deactivateCountry(isoCode);
        return ResponseEntity.ok(response);
    }
}
