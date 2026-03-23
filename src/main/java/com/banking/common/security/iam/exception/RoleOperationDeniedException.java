package com.banking.common.security.iam.exception;

import com.banking.common.message.MessageCatalog;

public class RoleOperationDeniedException extends RuntimeException {
    private final String messageCode;

    public RoleOperationDeniedException(String messageCode) {
        super(MessageCatalog.getMessage(messageCode));
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public static RoleOperationDeniedException cannotDeleteSystemRole() {
        return new RoleOperationDeniedException("IAM_003");
    }

    public static RoleOperationDeniedException cannotModifySystemRole() {
        return new RoleOperationDeniedException("IAM_004");
    }
}