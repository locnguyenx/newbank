package com.banking.common.security.auth;

import com.banking.common.security.auth.dto.LoginRequest;
import com.banking.common.security.auth.dto.RefreshTokenRequest;
import com.banking.common.security.auth.dto.TokenResponse;
import com.banking.common.security.auth.exception.InvalidCredentialsException;
import com.banking.common.security.config.JwtConfig;
import com.banking.common.security.entity.RefreshToken;
import com.banking.common.security.entity.RefreshTokenRepository;
import com.banking.common.security.entity.User;
import com.banking.common.security.entity.UserRepository;
import com.banking.common.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtTokenProvider jwtTokenProvider,
            JwtConfig jwtConfig,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::invalidCredentials);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw InvalidCredentialsException.invalidCredentials();
        }

        if (user.getStatus() != com.banking.common.security.entity.UserStatus.ACTIVE) {
            throw InvalidCredentialsException.invalidCredentials();
        }

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getEmail(),
                user.getUserType().name()
        );

        String refreshTokenValue = jwtTokenProvider.generateRefreshToken(user.getId());

        RefreshToken refreshToken = new RefreshToken(
                user.getId(),
                refreshTokenValue,
                Instant.now().plusMillis(jwtConfig.getRefreshTokenExpiry())
        );
        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(accessToken, refreshTokenValue, jwtConfig.getAccessTokenExpiry());
    }

    @Transactional
    public TokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidCredentialsException("Refresh token expired");
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getEmail(),
                user.getUserType().name()
        );

        String newRefreshTokenValue = jwtTokenProvider.generateRefreshToken(user.getId());

        refreshTokenRepository.delete(refreshToken);

        RefreshToken newRefreshToken = new RefreshToken(
                user.getId(),
                newRefreshTokenValue,
                Instant.now().plusMillis(jwtConfig.getRefreshTokenExpiry())
        );
        refreshTokenRepository.save(newRefreshToken);

        return new TokenResponse(accessToken, newRefreshTokenValue, jwtConfig.getAccessTokenExpiry());
    }
}