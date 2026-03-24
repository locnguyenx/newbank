package com.banking.cashmanagement.repository;

import com.banking.cashmanagement.domain.entity.PayrollRecord;
import com.banking.cashmanagement.domain.enums.PayrollRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {
    
    List<PayrollRecord> findByPayrollBatchId(Long payrollBatchId);
    
    List<PayrollRecord> findByPayrollBatchIdAndStatus(Long payrollBatchId, PayrollRecordStatus status);
    
    List<PayrollRecord> findByStatus(PayrollRecordStatus status);
}
