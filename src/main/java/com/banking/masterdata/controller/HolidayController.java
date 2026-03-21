package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateHolidayRequest;
import com.banking.masterdata.dto.response.HolidayResponse;
import com.banking.masterdata.service.HolidayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/master-data/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping
    public ResponseEntity<HolidayResponse> createHoliday(@Valid @RequestBody CreateHolidayRequest request) {
        HolidayResponse response = holidayService.createHoliday(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HolidayResponse>> getHolidays(
            @RequestParam String countryCode,
            @RequestParam int year) {
        List<HolidayResponse> response = holidayService.getHolidays(countryCode, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isHoliday(
            @RequestParam String countryCode,
            @RequestParam LocalDate date) {
        boolean isHoliday = holidayService.isHoliday(countryCode, date);
        return ResponseEntity.ok(isHoliday);
    }
}
