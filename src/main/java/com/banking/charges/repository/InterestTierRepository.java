package com.banking.charges.repository;

import com.banking.charges.domain.entity.InterestTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestTierRepository extends JpaRepository<InterestTier, Long> {
    List<InterestTier> findByInterestRateIdOrderByBalanceFrom(Long interestRateId);
    void deleteByInterestRateId(Long interestRateId);
}
