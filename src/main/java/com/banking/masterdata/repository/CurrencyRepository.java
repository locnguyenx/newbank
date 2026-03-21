package com.banking.masterdata.repository;

import com.banking.masterdata.domain.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {

    List<Currency> findByIsActiveTrue();

    boolean existsByCode(String code);
}
