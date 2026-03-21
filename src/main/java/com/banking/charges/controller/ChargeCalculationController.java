package com.banking.charges.controller;

import com.banking.charges.dto.request.ChargeCalculationRequest;
import com.banking.charges.dto.response.ChargeCalculationResponse;
import com.banking.charges.service.ChargeCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/charges")
public class ChargeCalculationController {

    private final ChargeCalculationService calculationService;

    public ChargeCalculationController(ChargeCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<ChargeCalculationResponse> calculate(@RequestBody ChargeCalculationRequest request) {
        ChargeCalculationResponse response = calculationService.calculate(request);
        return ResponseEntity.ok(response);
    }
}
