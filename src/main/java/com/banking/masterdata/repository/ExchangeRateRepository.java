package com.banking.masterdata.repository;

import com.banking.masterdata.domain.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findTopByBaseCurrencyCodeAndTargetCurrencyCodeOrderByEffectiveDateDesc(String base, String target);
}
