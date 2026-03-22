package com.banking.common.exception;

import com.banking.common.message.MessageCatalog;

public class BaseException extends RuntimeException {
    private final String messageCode;
    
    public BaseException(String messageCode) {
        super(MessageCatalog.getMessage(messageCode));
        this.messageCode = messageCode;
    }
    
    public BaseException(String messageCode, String customMessage) {
        super(customMessage);
        this.messageCode = messageCode;
    }
    
    public String getMessageCode() {
        return messageCode;
    }
}
