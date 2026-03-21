package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateIndustryRequest;
import com.banking.masterdata.dto.response.IndustryResponse;
import com.banking.masterdata.service.IndustryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-data/industries")
public class IndustryController {

    private final IndustryService industryService;

    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @PostMapping
    public ResponseEntity<IndustryResponse> createIndustry(@Valid @RequestBody CreateIndustryRequest request) {
        IndustryResponse response = industryService.createIndustry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IndustryResponse>> getAllIndustries(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<IndustryResponse> response = industryService.getAllIndustries(activeOnly);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<IndustryResponse> getIndustry(@PathVariable String code) {
        IndustryResponse response = industryService.getIndustry(code);
        return ResponseEntity.ok(response);
    }
}
