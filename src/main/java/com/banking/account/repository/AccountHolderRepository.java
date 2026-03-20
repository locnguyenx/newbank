package com.banking.account.repository;

import com.banking.account.domain.entity.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

    List<AccountHolder> findByAccount_Id(Long accountId);

    List<AccountHolder> findByCustomer_Id(Long customerId);

    Optional<AccountHolder> findByAccount_IdAndCustomer_IdAndStatus(Long accountId, Long customerId,
                                                                    com.banking.account.domain.enums.AccountHolderStatus status);
}
