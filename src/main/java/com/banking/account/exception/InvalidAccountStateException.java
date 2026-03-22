package com.banking.account.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class InvalidAccountStateException extends BaseException {
    public InvalidAccountStateException(String messageCode, String message) {
        super(messageCode, message);
    }
    
    public static InvalidAccountStateException alreadyClosed() {
        return new InvalidAccountStateException(MessageCatalog.ACCOUNT_ALREADY_CLOSED, MessageCatalog.getMessage(MessageCatalog.ACCOUNT_ALREADY_CLOSED));
    }
    
    public static InvalidAccountStateException cannotCloseFrozen() {
        return new InvalidAccountStateException(MessageCatalog.ACCOUNT_CANNOT_CLOSE_FROZEN, MessageCatalog.getMessage(MessageCatalog.ACCOUNT_CANNOT_CLOSE_FROZEN));
    }
    
    public static InvalidAccountStateException nonZeroBalance() {
        return new InvalidAccountStateException(MessageCatalog.ACCOUNT_NON_ZERO_BALANCE, MessageCatalog.getMessage(MessageCatalog.ACCOUNT_NON_ZERO_BALANCE));
    }
    
    public static InvalidAccountStateException cannotFreezeClosed() {
        return new InvalidAccountStateException(MessageCatalog.ACCOUNT_CANNOT_FREEZE_CLOSED, MessageCatalog.getMessage(MessageCatalog.ACCOUNT_CANNOT_FREEZE_CLOSED));
    }
    
    public static InvalidAccountStateException notFrozen() {
        return new InvalidAccountStateException(MessageCatalog.ACCOUNT_NOT_FROZEN, MessageCatalog.getMessage(MessageCatalog.ACCOUNT_NOT_FROZEN));
    }
    
    public static InvalidAccountStateException alreadyHolderActive() {
        return new InvalidAccountStateException(MessageCatalog.INTERNAL_ERROR, "Customer is already an active account holder");
    }
    
    public static InvalidAccountStateException holderAlreadyRemoved() {
        return new InvalidAccountStateException(MessageCatalog.INTERNAL_ERROR, "Account holder already removed");
    }
    
    public static InvalidAccountStateException inactiveHolderRole() {
        return new InvalidAccountStateException(MessageCatalog.INTERNAL_ERROR, "Cannot update role of inactive holder");
    }
}
