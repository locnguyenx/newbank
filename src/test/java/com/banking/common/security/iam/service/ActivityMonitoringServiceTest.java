package com.banking.common.security.iam.service;

import com.banking.common.security.iam.dto.LoginHistoryResponse;
import com.banking.common.security.iam.entity.FailedLoginAttempt;
import com.banking.common.security.iam.entity.LoginHistory;
import com.banking.common.security.iam.repository.FailedLoginAttemptRepository;
import com.banking.common.security.iam.repository.LoginHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityMonitoringServiceTest {

    @Mock
    private LoginHistoryRepository loginHistoryRepository;

    @Mock
    private FailedLoginAttemptRepository failedLoginAttemptRepository;

    @InjectMocks
    private ActivityMonitoringService activityMonitoringService;

    private LoginHistory successfulLogin;
    private LoginHistory failedLogin;
    private FailedLoginAttempt failedAttempt;

    @BeforeEach
    void setUp() {
        successfulLogin = new LoginHistory(1L, "PASSWORD", "192.168.1.1", "Mozilla", true);
        successfulLogin.setId(1L);

        failedLogin = new LoginHistory(1L, "PASSWORD", "192.168.1.1", "Mozilla", false);
        failedLogin.setId(2L);

        failedAttempt = new FailedLoginAttempt("test@example.com", "192.168.1.1", "Invalid password");
        failedAttempt.setId(1L);
    }

    @Test
    void recordLogin_Success() {
        when(loginHistoryRepository.save(any(LoginHistory.class))).thenReturn(successfulLogin);

        activityMonitoringService.recordLogin(1L, "PASSWORD", "192.168.1.1", "Mozilla", true);

        verify(loginHistoryRepository).save(any(LoginHistory.class));
    }

    @Test
    void recordFailedLogin_Success() {
        when(failedLoginAttemptRepository.save(any(FailedLoginAttempt.class))).thenReturn(failedAttempt);

        activityMonitoringService.recordFailedLogin("test@example.com", "192.168.1.1", "Invalid password");

        verify(failedLoginAttemptRepository).save(any(FailedLoginAttempt.class));
    }

    @Test
    void getUserLoginHistory_Success() {
        when(loginHistoryRepository.findByUserIdOrderByTimestampDesc(1L))
                .thenReturn(Arrays.asList(successfulLogin, failedLogin));

        List<LoginHistoryResponse> result = activityMonitoringService.getUserLoginHistory(1L);

        assertEquals(2, result.size());
    }

    @Test
    void getAllLoginHistory_Success() {
        Page<LoginHistory> page = new PageImpl<>(Arrays.asList(successfulLogin, failedLogin));
        when(loginHistoryRepository.findAllByOrderByTimestampDesc(any(PageRequest.class))).thenReturn(page);

        Page<LoginHistoryResponse> result = activityMonitoringService.getAllLoginHistory(PageRequest.of(0, 10));

        assertEquals(2, result.getContent().size());
    }

    @Test
    void getFailedLogins_Success() {
        when(failedLoginAttemptRepository.findByEmailOrderByAttemptTimeDesc("test@example.com"))
                .thenReturn(Collections.singletonList(failedAttempt));

        List<FailedLoginAttempt> result = activityMonitoringService.getFailedLogins("test@example.com");

        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }

    @Test
    void getFailedLoginCount_Success() {
        when(failedLoginAttemptRepository.countRecentAttemptsByEmail(eq("test@example.com"), any(Instant.class)))
                .thenReturn(5L);

        long count = activityMonitoringService.getFailedLoginCount("test@example.com", 30);

        assertEquals(5L, count);
    }

    @Test
    void cleanupOldFailedLogins_Success() {
        doNothing().when(failedLoginAttemptRepository).deleteByAttemptTimeBefore(any(Instant.class));

        activityMonitoringService.cleanupOldFailedLogins(30);

        verify(failedLoginAttemptRepository).deleteByAttemptTimeBefore(any(Instant.class));
    }
}