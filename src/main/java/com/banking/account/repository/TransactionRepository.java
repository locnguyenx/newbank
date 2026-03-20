package com.banking.account.repository;

import com.banking.account.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber " +
           "AND t.transactionDate BETWEEN :from AND :to ORDER BY t.transactionDate DESC")
    Page<Transaction> findByAccount_AccountNumberAndTransactionDateBetween(
            @Param("accountNumber") String accountNumber,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    boolean existsByAccount_AccountNumber(String accountNumber);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.accountNumber = :accountNumber")
    long countByAccount_AccountNumber(@Param("accountNumber") String accountNumber);
}
