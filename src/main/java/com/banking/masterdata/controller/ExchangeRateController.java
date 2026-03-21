package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateExchangeRateRequest;
import com.banking.masterdata.dto.response.ExchangeRateResponse;
import com.banking.masterdata.service.ExchangeRateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/master-data/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping
    public ResponseEntity<ExchangeRateResponse> createExchangeRate(@Valid @RequestBody CreateExchangeRateRequest request) {
        ExchangeRateResponse response = exchangeRateService.createExchangeRate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/latest")
    public ResponseEntity<ExchangeRateResponse> getLatestRate(
            @RequestParam String base,
            @RequestParam String target) {
        ExchangeRateResponse response = exchangeRateService.getLatestRate(base, target);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/convert")
    public ResponseEntity<BigDecimal> convertAmount(
            @RequestParam String base,
            @RequestParam String target,
            @RequestParam BigDecimal amount) {
        BigDecimal converted = exchangeRateService.convertAmount(base, target, amount);
        return ResponseEntity.ok(converted);
    }
}
