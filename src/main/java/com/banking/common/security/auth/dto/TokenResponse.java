package com.banking.common.security.auth.dto;

public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private boolean mfaRequired;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.mfaRequired = false;
    }

    public static TokenResponse mfaRequired() {
        TokenResponse response = new TokenResponse();
        response.accessToken = null;
        response.refreshToken = null;
        response.expiresIn = 0;
        response.mfaRequired = true;
        return response;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public boolean isMfaRequired() {
        return mfaRequired;
    }

    public void setMfaRequired(boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
    }
}
