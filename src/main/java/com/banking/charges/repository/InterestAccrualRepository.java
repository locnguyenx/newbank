package com.banking.charges.repository;

import com.banking.charges.domain.entity.InterestAccrual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterestAccrualRepository extends JpaRepository<InterestAccrual, Long> {
    Optional<InterestAccrual> findByInterestRateIdAndAccountNumberAndAccrualDate(Long interestRateId, String accountNumber, LocalDate accrualDate);
    List<InterestAccrual> findByAccountNumberAndApplied(String accountNumber, Boolean applied);
    List<InterestAccrual> findByAccrualDateAndApplied(LocalDate accrualDate, Boolean applied);
}
