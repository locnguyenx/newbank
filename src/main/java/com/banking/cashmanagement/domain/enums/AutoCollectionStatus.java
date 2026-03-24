package com.banking.cashmanagement.domain.enums;

public enum AutoCollectionStatus {
    PENDING,
    SUCCESS,
    FAILED_INSUFFICIENT_FUNDS,
    FAILED_INVALID_ACCOUNT,
    RETRY_SCHEDULED,
    CANCELLED
}
