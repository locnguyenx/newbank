package com.banking.masterdata.repository;

import com.banking.masterdata.domain.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {

    List<DocumentType> findByIsActiveTrue();

    List<DocumentType> findByIsActiveTrueAndCategory(String category);
}
