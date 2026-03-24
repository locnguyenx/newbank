package com.banking.common.security.mfa;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class MfaService {

    private final MfaSecretRepository mfaSecretRepository;
    private final CodeVerifier codeVerifier;
    private final SecretGenerator secretGenerator;

    public MfaService() {
        this.mfaSecretRepository = null;
        this.secretGenerator = new DefaultSecretGenerator();
        this.codeVerifier = createDefaultCodeVerifier();
    }

    public MfaService(MfaSecretRepository mfaSecretRepository) {
        this(mfaSecretRepository, createDefaultCodeVerifier());
    }

    public MfaService(MfaSecretRepository mfaSecretRepository, CodeVerifier codeVerifier) {
        this.mfaSecretRepository = mfaSecretRepository;
        this.secretGenerator = new DefaultSecretGenerator();
        this.codeVerifier = codeVerifier;
    }

    private static CodeVerifier createDefaultCodeVerifier() {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setAllowedTimePeriodDiscrepancy(1);
        return verifier;
    }

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public boolean verifyCode(Long userId, String code) {
        Optional<MfaSecret> mfaSecretOpt = mfaSecretRepository.findByUserId(userId);
        if (mfaSecretOpt.isEmpty()) {
            return false;
        }

        MfaSecret mfaSecret = mfaSecretOpt.get();
        return codeVerifier.isValidCode(mfaSecret.getSecret(), code);
    }

    public void enableMfa(Long userId) {
        Optional<MfaSecret> mfaSecretOpt = mfaSecretRepository.findByUserId(userId);
        if (mfaSecretOpt.isPresent()) {
            MfaSecret mfaSecret = mfaSecretOpt.get();
            mfaSecret.setEnabled(true);
            mfaSecretRepository.save(mfaSecret);
        }
    }

    public void disableMfa(Long userId) {
        Optional<MfaSecret> mfaSecretOpt = mfaSecretRepository.findByUserId(userId);
        if (mfaSecretOpt.isPresent()) {
            MfaSecret mfaSecret = mfaSecretOpt.get();
            mfaSecret.setEnabled(false);
            mfaSecret.setVerified(false);
            mfaSecretRepository.save(mfaSecret);
        }
    }

    public boolean isMfaEnabled(Long userId) {
        return mfaSecretRepository.findByUserId(userId)
                .map(MfaSecret::isEnabled)
                .orElse(false);
    }

    public Optional<MfaSecret> getMfaSecret(Long userId) {
        return mfaSecretRepository.findByUserId(userId);
    }

    public MfaSecret enrollMfa(Long userId) {
        Optional<MfaSecret> existing = mfaSecretRepository.findByUserId(userId);
        if (existing.isPresent()) {
            MfaSecret mfaSecret = existing.get();
            mfaSecret.setSecret(generateSecret());
            mfaSecret.setVerified(false);
            mfaSecret.setEnabled(false);
            return mfaSecretRepository.save(mfaSecret);
        }

        MfaSecret mfaSecret = new MfaSecret(userId, generateSecret());
        return mfaSecretRepository.save(mfaSecret);
    }

    public void verifyAndEnableMfa(Long userId, String code) {
        Optional<MfaSecret> mfaSecretOpt = mfaSecretRepository.findByUserId(userId);
        if (mfaSecretOpt.isEmpty()) {
            throw new IllegalStateException("MFA not enrolled for user");
        }

        MfaSecret mfaSecret = mfaSecretOpt.get();
        if (!codeVerifier.isValidCode(mfaSecret.getSecret(), code)) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        mfaSecret.setVerified(true);
        mfaSecret.setEnabled(true);
        mfaSecretRepository.save(mfaSecret);
    }
}
