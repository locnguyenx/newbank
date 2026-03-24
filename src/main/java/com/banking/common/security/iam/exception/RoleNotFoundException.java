package com.banking.common.security.iam.exception;

import com.banking.common.message.MessageCatalog;

public class RoleNotFoundException extends RuntimeException {
    private final String messageCode;

    public RoleNotFoundException(String messageCode) {
        super(MessageCatalog.getMessage(messageCode));
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public static RoleNotFoundException notFound(Long id) {
        return new RoleNotFoundException("IAM_001");
    }

    public static RoleNotFoundException notFoundByName(String name) {
        return new RoleNotFoundException("IAM_002");
    }
}