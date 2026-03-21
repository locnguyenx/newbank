package com.banking.masterdata.dto.response;

import com.banking.masterdata.domain.entity.Holiday;

import java.time.LocalDate;

public class HolidayResponse {

    private Long id;
    private String countryCode;
    private LocalDate holidayDate;
    private String description;

    public HolidayResponse() {
    }

    public static HolidayResponse fromEntity(Holiday holiday) {
        HolidayResponse response = new HolidayResponse();
        response.id = holiday.getId();
        response.countryCode = holiday.getCountry().getIsoCode();
        response.holidayDate = holiday.getHolidayDate();
        response.description = holiday.getDescription();
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
