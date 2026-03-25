package com.banking.common.security.iam.repository;

import com.banking.common.security.iam.entity.FailedLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, Long> {
    List<FailedLoginAttempt> findByEmailOrderByAttemptTimeDesc(String email);
    List<FailedLoginAttempt> findByIpAddressOrderByAttemptTimeDesc(String ipAddress);
    List<FailedLoginAttempt> findAllByOrderByAttemptTimeDesc();
    
    @Query("SELECT f FROM FailedLoginAttempt f WHERE f.attemptTime > ?1")
    List<FailedLoginAttempt> findRecentAttempts(Instant since);
    
    @Query("SELECT COUNT(f) FROM FailedLoginAttempt f WHERE f.email = ?1 AND f.attemptTime > ?2")
    long countRecentAttemptsByEmail(String email, Instant since);
    
    void deleteByAttemptTimeBefore(Instant before);
}