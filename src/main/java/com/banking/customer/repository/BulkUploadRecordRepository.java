package com.banking.customer.repository;

import com.banking.customer.domain.entity.BulkUploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkUploadRecordRepository extends JpaRepository<BulkUploadRecord, Long> {
}