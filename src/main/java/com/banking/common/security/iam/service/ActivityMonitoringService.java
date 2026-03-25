package com.banking.common.security.iam.service;

import com.banking.common.security.iam.dto.LoginHistoryResponse;
import com.banking.common.security.iam.entity.FailedLoginAttempt;
import com.banking.common.security.iam.entity.LoginHistory;
import com.banking.common.security.iam.repository.FailedLoginAttemptRepository;
import com.banking.common.security.iam.repository.LoginHistoryRepository;
import com.banking.common.security.entity.User;
import com.banking.common.security.entity.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityMonitoringService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;
    private final UserRepository userRepository;

    public ActivityMonitoringService(LoginHistoryRepository loginHistoryRepository,
                                       FailedLoginAttemptRepository failedLoginAttemptRepository,
                                       UserRepository userRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.failedLoginAttemptRepository = failedLoginAttemptRepository;
        this.userRepository = userRepository;
    }

    public void recordLogin(Long userId, String loginType, String ipAddress, String userAgent, boolean success) {
        LoginHistory loginHistory = new LoginHistory(userId, loginType, ipAddress, userAgent, success);
        loginHistoryRepository.save(loginHistory);
    }

    public void recordFailedLogin(String email, String ipAddress, String reason) {
        FailedLoginAttempt attempt = new FailedLoginAttempt(email, ipAddress, reason);
        failedLoginAttemptRepository.save(attempt);
    }

    @Transactional(readOnly = true)
    public List<LoginHistoryResponse> getUserLoginHistory(Long userId) {
        return loginHistoryRepository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<LoginHistoryResponse> getAllLoginHistory(Pageable pageable) {
        return loginHistoryRepository.findAllByOrderByTimestampDesc(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<FailedLoginAttempt> getFailedLogins(String email) {
        return failedLoginAttemptRepository.findByEmailOrderByAttemptTimeDesc(email);
    }

    @Transactional(readOnly = true)
    public long getFailedLoginCount(String email, int minutes) {
        Instant since = Instant.now().minus(minutes, ChronoUnit.MINUTES);
        return failedLoginAttemptRepository.countRecentAttemptsByEmail(email, since);
    }

    public void cleanupOldFailedLogins(int daysOld) {
        Instant cutoff = Instant.now().minus(daysOld, ChronoUnit.DAYS);
        failedLoginAttemptRepository.deleteByAttemptTimeBefore(cutoff);
    }

    private LoginHistoryResponse toResponse(LoginHistory loginHistory) {
        LoginHistoryResponse response = new LoginHistoryResponse();
        response.setId(loginHistory.getId());
        response.setUserId(loginHistory.getUserId());
        
        userRepository.findById(loginHistory.getUserId())
                .ifPresent(user -> response.setEmail(user.getEmail()));
        
        response.setLoginType(loginHistory.getLoginType());
        response.setIpAddress(loginHistory.getIpAddress());
        response.setUserAgent(loginHistory.getUserAgent());
        response.setSuccess(loginHistory.isSuccess());
        response.setTimestamp(loginHistory.getTimestamp());
        return response;
    }
}