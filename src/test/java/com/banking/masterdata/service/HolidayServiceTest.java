package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.domain.entity.Holiday;
import com.banking.masterdata.dto.request.CreateHolidayRequest;
import com.banking.masterdata.dto.response.HolidayResponse;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.CountryRepository;
import com.banking.masterdata.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        holidayService = new HolidayService(holidayRepository, countryRepository, masterDataMapper);
    }

    private CreateHolidayRequest createRequest(String countryCode, LocalDate date, String description) {
        CreateHolidayRequest request = new CreateHolidayRequest();
        request.setCountryCode(countryCode);
        request.setHolidayDate(date);
        request.setDescription(description);
        return request;
    }

    @Test
    void createHoliday_success() {
        Country country = new Country("US", "United States", "North America");
        CreateHolidayRequest request = createRequest("US", LocalDate.of(2026, 7, 4), "Independence Day");
        Holiday holiday = new Holiday(country, LocalDate.of(2026, 7, 4), "Independence Day");

        when(countryRepository.findById("US")).thenReturn(Optional.of(country));
        when(holidayRepository.save(any())).thenReturn(holiday);
        when(masterDataMapper.toResponse(any(Holiday.class))).thenReturn(HolidayResponse.fromEntity(holiday));

        HolidayResponse response = holidayService.createHoliday(request);

        assertEquals("US", response.getCountryCode());
        assertEquals(LocalDate.of(2026, 7, 4), response.getHolidayDate());
        assertEquals("Independence Day", response.getDescription());

        verify(holidayRepository).save(any());
    }

    @Test
    void isHoliday_true() {
        when(holidayRepository.existsByCountryIsoCodeAndHolidayDate("US", LocalDate.of(2026, 7, 4)))
                .thenReturn(true);

        boolean result = holidayService.isHoliday("US", LocalDate.of(2026, 7, 4));

        assertTrue(result);
    }

    @Test
    void isHoliday_false() {
        when(holidayRepository.existsByCountryIsoCodeAndHolidayDate("US", LocalDate.of(2026, 3, 15)))
                .thenReturn(false);

        boolean result = holidayService.isHoliday("US", LocalDate.of(2026, 3, 15));

        assertFalse(result);
    }

    @Test
    void getHolidays_returnsCorrectCount() {
        Country country = new Country("US", "United States", "North America");
        Holiday holiday1 = new Holiday(country, LocalDate.of(2026, 1, 1), "New Year's Day");
        Holiday holiday2 = new Holiday(country, LocalDate.of(2026, 7, 4), "Independence Day");
        Holiday holiday3 = new Holiday(country, LocalDate.of(2026, 12, 25), "Christmas Day");

        when(holidayRepository.findByCountryIsoCodeAndHolidayDateBetween(eq("US"), eq(LocalDate.of(2026, 1, 1)), eq(LocalDate.of(2026, 12, 31))))
                .thenReturn(Arrays.asList(holiday1, holiday2, holiday3));
        when(masterDataMapper.toResponse(holiday1)).thenReturn(HolidayResponse.fromEntity(holiday1));
        when(masterDataMapper.toResponse(holiday2)).thenReturn(HolidayResponse.fromEntity(holiday2));
        when(masterDataMapper.toResponse(holiday3)).thenReturn(HolidayResponse.fromEntity(holiday3));

        List<HolidayResponse> responses = holidayService.getHolidays("US", 2026);

        assertEquals(3, responses.size());
        assertEquals("New Year's Day", responses.get(0).getDescription());
        assertEquals("Independence Day", responses.get(1).getDescription());
        assertEquals("Christmas Day", responses.get(2).getDescription());
    }
}
