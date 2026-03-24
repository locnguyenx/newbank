package com.banking.common.security.iam.repository;

import com.banking.common.security.iam.entity.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findByUserIdOrderByTimestampDesc(Long userId);
    Page<LoginHistory> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
    Page<LoginHistory> findAllByOrderByTimestampDesc(Pageable pageable);
    List<LoginHistory> findByUserIdAndTimestampAfter(Long userId, Instant after);
}