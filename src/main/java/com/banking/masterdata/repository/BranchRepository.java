package com.banking.masterdata.repository;

import com.banking.masterdata.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {

    List<Branch> findByIsActiveTrue();

    List<Branch> findByIsActiveTrueAndCountryCode(String countryCode);
}
