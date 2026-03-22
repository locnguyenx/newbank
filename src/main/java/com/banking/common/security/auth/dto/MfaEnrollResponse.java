package com.banking.common.security.auth.dto;

public class MfaEnrollResponse {

    private String secret;
    private String qrCodeUrl;

    public MfaEnrollResponse() {
    }

    public MfaEnrollResponse(String secret, String qrCodeUrl) {
        this.secret = secret;
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
