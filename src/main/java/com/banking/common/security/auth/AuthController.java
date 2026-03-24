package com.banking.common.security.auth;

import com.banking.common.security.auth.dto.LoginRequest;
import com.banking.common.security.auth.dto.MfaEnrollResponse;
import com.banking.common.security.auth.dto.MfaRequest;
import com.banking.common.security.auth.dto.RefreshTokenRequest;
import com.banking.common.security.auth.dto.TokenResponse;
import com.banking.common.security.mfa.MfaService;
import com.banking.common.security.mfa.MfaSecret;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MfaService mfaService;

    public AuthController(AuthService authService, MfaService mfaService) {
        this.authService = authService;
        this.mfaService = mfaService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            TokenResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            TokenResponse response = authService.refresh(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/mfa/enroll")
    public ResponseEntity<MfaEnrollResponse> enrollMfa() {
        Long userId = getCurrentUserId();
        MfaSecret mfaSecret = mfaService.enrollMfa(userId);
        String qrCodeUrl = String.format("otpauth://totp/BankingApp:user_%d?secret=%s&issuer=BankingApp",
                userId, mfaSecret.getSecret());
        return ResponseEntity.ok(new MfaEnrollResponse(mfaSecret.getSecret(), qrCodeUrl));
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<?> verifyMfa(@Valid @RequestBody MfaRequest request) {
        Long userId = getCurrentUserId();
        boolean valid = mfaService.verifyCode(userId, request.getCode());
        if (valid) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "message", "Invalid MFA code"));
        }
    }

    @PostMapping("/mfa/enable")
    public ResponseEntity<?> enableMfa(@Valid @RequestBody MfaRequest request) {
        Long userId = getCurrentUserId();
        try {
            mfaService.verifyAndEnableMfa(userId, request.getCode());
            return ResponseEntity.ok(Map.of("enabled", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("enabled", false, "message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("enabled", false, "message", "MFA not enrolled"));
        }
    }

    @PostMapping("/mfa/disable")
    public ResponseEntity<?> disableMfa() {
        Long userId = getCurrentUserId();
        mfaService.disableMfa(userId);
        return ResponseEntity.ok(Map.of("enabled", false));
    }

    @PostMapping("/mfa/status")
    public ResponseEntity<?> getMfaStatus() {
        Long userId = getCurrentUserId();
        boolean enabled = mfaService.isMfaEnabled(userId);
        return ResponseEntity.ok(Map.of("enabled", enabled));
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        throw new BadCredentialsException("User not authenticated");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().build();
    }
}