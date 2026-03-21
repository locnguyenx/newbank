package com.banking.masterdata.repository;

import com.banking.masterdata.domain.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, String> {

    List<Industry> findByIsActiveTrue();

    List<Industry> findByParentCode(String parentCode);
}
