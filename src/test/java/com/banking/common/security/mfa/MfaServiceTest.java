package com.banking.common.security.mfa;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MfaServiceTest {

    @Mock
    private MfaSecretRepository mfaSecretRepository;
    
    private MfaService mfaService;
    private SecretGenerator secretGenerator;
    private CodeVerifier codeVerifier;
    private CodeGenerator codeGenerator;
    private TimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        timeProvider = new SystemTimeProvider();
        codeGenerator = new DefaultCodeGenerator();
        codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        ((DefaultCodeVerifier) codeVerifier).setAllowedTimePeriodDiscrepancy(1);
        
        mfaService = new MfaService(mfaSecretRepository, codeVerifier);
        secretGenerator = new DefaultSecretGenerator();
    }

    @Test
    void generateSecret_returnsValidBase32Secret() {
        String secret = mfaService.generateSecret();
        
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
    }

    @Test
    void verifyCode_withValidCode_returnsTrue() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(false);
        mfaSecret.setVerified(false);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));

        String code = codeGenerator.generate(secret, timeProvider.getTime());
        
        boolean result = mfaService.verifyCode(userId, code);
        
        assertTrue(result);
    }

    @Test
    void verifyCode_withInvalidCode_returnsFalse() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(false);
        mfaSecret.setVerified(false);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));
        
        boolean result = mfaService.verifyCode(userId, "000000");
        
        assertFalse(result);
    }

    @Test
    void verifyCode_withNonEnrolledUser_returnsFalse() {
        when(mfaSecretRepository.findByUserId(999L)).thenReturn(Optional.empty());
        
        boolean result = mfaService.verifyCode(999L, "123456");
        
        assertFalse(result);
    }

    @Test
    void isMfaEnabled_whenEnabled_returnsTrue() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(true);
        mfaSecret.setVerified(true);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));
        
        boolean result = mfaService.isMfaEnabled(userId);
        
        assertTrue(result);
    }

    @Test
    void isMfaEnabled_whenDisabled_returnsFalse() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(false);
        mfaSecret.setVerified(false);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));
        
        boolean result = mfaService.isMfaEnabled(userId);
        
        assertFalse(result);
    }

    @Test
    void isMfaEnabled_whenNotEnrolled_returnsFalse() {
        when(mfaSecretRepository.findByUserId(999L)).thenReturn(Optional.empty());
        
        boolean result = mfaService.isMfaEnabled(999L);
        
        assertFalse(result);
    }

    @Test
    void enableMfa_enablesMfaForVerifiedUser() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(false);
        mfaSecret.setVerified(true);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));
        when(mfaSecretRepository.save(any(MfaSecret.class))).thenReturn(mfaSecret);

        mfaService.enableMfa(userId);

        verify(mfaSecretRepository).save(any(MfaSecret.class));
    }

    @Test
    void enableMfa_doesNothingWhenNotEnrolled() {
        Long userId = 999L;
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.empty());
        
        assertDoesNotThrow(() -> mfaService.enableMfa(userId));
        
        verify(mfaSecretRepository, never()).save(any());
    }

    @Test
    void disableMfa_disablesMfa() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(true);
        mfaSecret.setVerified(true);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));
        when(mfaSecretRepository.save(any(MfaSecret.class))).thenReturn(mfaSecret);

        mfaService.disableMfa(userId);

        verify(mfaSecretRepository).save(any(MfaSecret.class));
    }

    @Test
    void disableMfa_doesNothingWhenNotEnrolled() {
        Long userId = 999L;
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.empty());
        
        assertDoesNotThrow(() -> mfaService.disableMfa(userId));
        
        verify(mfaSecretRepository, never()).save(any());
    }

    @Test
    void enrollMfa_createsNewMfaSecret() {
        Long userId = 1L;
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(mfaSecretRepository.save(any(MfaSecret.class))).thenAnswer(inv -> inv.getArgument(0));

        MfaSecret result = mfaService.enrollMfa(userId);
        
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getSecret());
        assertFalse(result.isEnabled());
        assertFalse(result.isVerified());
    }

    @Test
    void enrollMfa_regeneratesSecretForExistingUser() {
        Long userId = 1L;
        String originalSecret = secretGenerator.generate();
        MfaSecret original = new MfaSecret(userId, originalSecret);
        original.setEnabled(true);
        original.setVerified(true);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(original));
        when(mfaSecretRepository.save(any(MfaSecret.class))).thenAnswer(inv -> inv.getArgument(0));
        
        MfaSecret result = mfaService.enrollMfa(userId);
        
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertFalse(result.isEnabled());
        assertFalse(result.isVerified());
    }

    @Test
    void verifyAndEnableMfa_withValidCode_enablesMfa() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(false);
        mfaSecret.setVerified(false);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));
        when(mfaSecretRepository.save(any(MfaSecret.class))).thenAnswer(inv -> inv.getArgument(0));

        String code = codeGenerator.generate(secret, timeProvider.getTime());
        
        mfaService.verifyAndEnableMfa(userId, code);

        verify(mfaSecretRepository).save(argThat(s -> s.isEnabled() && s.isVerified()));
    }

    @Test
    void verifyAndEnableMfa_withInvalidCode_throwsException() {
        Long userId = 1L;
        String secret = secretGenerator.generate();
        MfaSecret mfaSecret = new MfaSecret(userId, secret);
        mfaSecret.setEnabled(false);
        mfaSecret.setVerified(false);
        
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.of(mfaSecret));

        assertThrows(IllegalArgumentException.class, 
            () -> mfaService.verifyAndEnableMfa(userId, "000000"));
    }

    @Test
    void verifyAndEnableMfa_whenNotEnrolled_throwsException() {
        Long userId = 999L;
        when(mfaSecretRepository.findByUserId(userId)).thenReturn(Optional.empty());
        
        assertThrows(IllegalStateException.class, 
            () -> mfaService.verifyAndEnableMfa(userId, "123456"));
    }
}
