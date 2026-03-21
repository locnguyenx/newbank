package com.banking.masterdata.repository;

import com.banking.masterdata.domain.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByCountryCodeAndHolidayDateBetween(String countryCode, LocalDate from, LocalDate to);

    boolean existsByCountryCodeAndHolidayDate(String countryCode, LocalDate date);
}
