package com.banking.account.repository;

import com.banking.account.domain.entity.Account;
import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomer_Id(Long customerId);

    List<Account> findByStatus(AccountStatus status);

    @Query("SELECT a FROM Account a WHERE " +
           "(:search IS NULL OR a.accountNumber LIKE %:search%) AND " +
           "(:type IS NULL OR a.type = :type) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:customerId IS NULL OR a.customer.id = :customerId)")
    Page<Account> searchAccounts(
        @Param("search") String search,
        @Param("type") AccountType type,
        @Param("status") AccountStatus status,
        @Param("customerId") Long customerId,
        Pageable pageable);
}
