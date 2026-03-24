package com.banking.cashmanagement.domain.enums;

public enum PaymentInstructionStatus {
    PENDING,
    VALID,
    INVALID,
    PROCESSING,
    SETTLED,
    FAILED,
    HELD_FOR_APPROVAL,
    CANCELLED
}
