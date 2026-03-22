package com.banking.common.exception;

import com.banking.common.message.MessageCatalog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseExceptionTest {

    @Test
    void baseExceptionShouldStoreMessageCode() {
        BaseException ex = new BaseException(MessageCatalog.ACCOUNT_NOT_FOUND);
        
        assertEquals(MessageCatalog.ACCOUNT_NOT_FOUND, ex.getMessageCode());
    }

    @Test
    void baseExceptionShouldUseMessageFromCatalog() {
        BaseException ex = new BaseException(MessageCatalog.ACCOUNT_NOT_FOUND);
        
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
        assertNotEquals(MessageCatalog.ACCOUNT_NOT_FOUND, ex.getMessage());
    }

    @Test
    void baseExceptionShouldSupportCustomMessage() {
        String customMessage = "Custom error for account: ABC123";
        BaseException ex = new BaseException(MessageCatalog.ACCOUNT_NOT_FOUND, customMessage);
        
        assertEquals(MessageCatalog.ACCOUNT_NOT_FOUND, ex.getMessageCode());
        assertEquals(customMessage, ex.getMessage());
    }
}
