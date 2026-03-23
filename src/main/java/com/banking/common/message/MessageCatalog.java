package com.banking.common.message;

import java.util.HashMap;
import java.util.Map;

public final class MessageCatalog {

    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_001";
    public static final String ACCOUNT_ALREADY_CLOSED = "ACCOUNT_002";
    public static final String ACCOUNT_CANNOT_CLOSE_FROZEN = "ACCOUNT_003";
    public static final String ACCOUNT_NON_ZERO_BALANCE = "ACCOUNT_004";
    public static final String ACCOUNT_CANNOT_FREEZE_CLOSED = "ACCOUNT_005";
    public static final String ACCOUNT_NOT_FROZEN = "ACCOUNT_006";
    public static final String ACCOUNT_ALREADY_EXISTS = "ACCOUNT_007";
    
    public static final String CUSTOMER_NOT_FOUND = "CUSTOMER_001";
    public static final String CUSTOMER_ALREADY_EXISTS = "CUSTOMER_002";
    
    public static final String PRODUCT_NOT_FOUND = "PRODUCT_001";
    public static final String PRODUCT_ALREADY_EXISTS = "PRODUCT_002";
    public static final String PRODUCT_INVALID_STATUS = "PRODUCT_003";
    public static final String PRODUCT_NOT_EDITABLE = "PRODUCT_004";
    public static final String PRODUCT_HAS_ACTIVE_CONTRACTS = "PRODUCT_005";
    public static final String MAKER_CHECKER_VIOLATION = "PRODUCT_006";
    public static final String INVALID_FEATURE_PREFIX = "PRODUCT_007";
    
    public static final String LIMIT_EXCEEDED = "LIMIT_001";
    public static final String LIMIT_NOT_FOUND = "LIMIT_002";
    
    public static final String AUTH_NOT_FOUND = "AUTH_007";
    public static final String AUTH_INVALID_CREDENTIALS = "AUTH_001";
    public static final String AUTH_TOKEN_EXPIRED = "AUTH_002";
    public static final String AUTH_INVALID_TOKEN = "AUTH_003";
    public static final String AUTH_MFA_REQUIRED = "AUTH_004";
    public static final String AUTH_MFA_INVALID = "AUTH_005";
    public static final String AUTH_ACCOUNT_LOCKED = "AUTH_006";
    
    public static final String KYC_NOT_FOUND = "KYC_001";
    public static final String KYC_INVALID_STATE = "KYC_002";
    
    public static final String CHRG_NOT_FOUND = "CHRG_001";
    public static final String CHRG_ALREADY_EXISTS = "CHRG_002";
    public static final String CHRG_INVALID_TYPE = "CHRG_003";
    
    public static final String MDATA_NOT_FOUND = "MDATA_001";
    public static final String MDATA_ALREADY_EXISTS = "MDATA_002";
    
    public static final String VALIDATION_ERROR = "VALIDATION_001";
    public static final String INTERNAL_ERROR = "SYSTEM_001";

    private static final Map<String, String> MESSAGES = new HashMap<>();
    
    static {
        MESSAGES.put(ACCOUNT_NOT_FOUND, "Account not found");
        MESSAGES.put(ACCOUNT_ALREADY_CLOSED, "This account is already closed");
        MESSAGES.put(ACCOUNT_CANNOT_CLOSE_FROZEN, "Cannot close a frozen account. Please unfreeze it first.");
        MESSAGES.put(ACCOUNT_NON_ZERO_BALANCE, "Cannot close account with non-zero balance. Please withdraw all funds first.");
        MESSAGES.put(ACCOUNT_CANNOT_FREEZE_CLOSED, "Cannot freeze a closed account");
        MESSAGES.put(ACCOUNT_NOT_FROZEN, "Account is not frozen");
        MESSAGES.put(ACCOUNT_ALREADY_EXISTS, "Account with this number already exists");
        
        MESSAGES.put(CUSTOMER_NOT_FOUND, "Customer not found");
        MESSAGES.put(CUSTOMER_ALREADY_EXISTS, "Customer with this ID already exists");
        
        MESSAGES.put(PRODUCT_NOT_FOUND, "Product not found");
        MESSAGES.put(PRODUCT_ALREADY_EXISTS, "Product with this code already exists");
        MESSAGES.put(PRODUCT_INVALID_STATUS, "Invalid product status for this operation");
        MESSAGES.put(PRODUCT_NOT_EDITABLE, "This product version cannot be edited");
        MESSAGES.put(PRODUCT_HAS_ACTIVE_CONTRACTS, "Product has active contracts and cannot be retired");
        MESSAGES.put(MAKER_CHECKER_VIOLATION, "You cannot approve your own changes");
        MESSAGES.put(INVALID_FEATURE_PREFIX, "Invalid feature code prefix");
        
        MESSAGES.put(LIMIT_EXCEEDED, "Transaction exceeds available limit");
        MESSAGES.put(LIMIT_NOT_FOUND, "Limit not found");
        
        MESSAGES.put(AUTH_NOT_FOUND, "Authorization not found");
        MESSAGES.put(AUTH_INVALID_CREDENTIALS, "Invalid email or password");
        MESSAGES.put(AUTH_TOKEN_EXPIRED, "Your session has expired. Please log in again.");
        MESSAGES.put(AUTH_INVALID_TOKEN, "Invalid or malformed authentication token");
        MESSAGES.put(AUTH_MFA_REQUIRED, "Multi-factor authentication required");
        MESSAGES.put(AUTH_MFA_INVALID, "Invalid multi-factor authentication code");
        MESSAGES.put(AUTH_ACCOUNT_LOCKED, "Your account has been locked. Please contact support.");
        
        MESSAGES.put(KYC_NOT_FOUND, "KYC record not found");
        MESSAGES.put(KYC_INVALID_STATE, "Invalid KYC state for this operation");
        
        MESSAGES.put(CHRG_NOT_FOUND, "Charge definition not found");
        MESSAGES.put(CHRG_ALREADY_EXISTS, "Charge definition already exists");
        MESSAGES.put(CHRG_INVALID_TYPE, "Invalid charge type");
        
        MESSAGES.put(MDATA_NOT_FOUND, "Record not found");
        MESSAGES.put(MDATA_ALREADY_EXISTS, "Record already exists");
        
        MESSAGES.put(VALIDATION_ERROR, "Please check your input and try again");
        MESSAGES.put(INTERNAL_ERROR, "An unexpected error occurred. Please try again later.");
    }
    
    private MessageCatalog() {}
    
    public static String getMessage(String code) {
        return MESSAGES.getOrDefault(code, "An unexpected error occurred");
    }
    
    public static String getMessage(String code, String defaultMessage) {
        return MESSAGES.getOrDefault(code, defaultMessage);
    }
}
