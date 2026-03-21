package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateCurrencyRequest;
import com.banking.masterdata.dto.request.UpdateCurrencyRequest;
import com.banking.masterdata.dto.response.CurrencyResponse;
import com.banking.masterdata.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-data/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<CurrencyResponse> createCurrency(@Valid @RequestBody CreateCurrencyRequest request) {
        CurrencyResponse response = currencyService.createCurrency(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<CurrencyResponse> response = currencyService.getAllCurrencies(activeOnly);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<CurrencyResponse> getCurrency(@PathVariable String code) {
        CurrencyResponse response = currencyService.getCurrency(code);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<CurrencyResponse> updateCurrency(
            @PathVariable String code,
            @Valid @RequestBody UpdateCurrencyRequest request) {
        CurrencyResponse response = currencyService.updateCurrency(code, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}/deactivate")
    public ResponseEntity<CurrencyResponse> deactivateCurrency(@PathVariable String code) {
        CurrencyResponse response = currencyService.deactivateCurrency(code);
        return ResponseEntity.ok(response);
    }
}
