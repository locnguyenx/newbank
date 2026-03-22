package com.banking.common.message;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MessageCatalogTest {

    @Test
    void allMessageCodesShouldBeUnique() {
        List<String> codes = getPublicStringConstants(MessageCatalog.class);

        long uniqueCount = codes.stream().distinct().count();
        assertEquals(codes.size(), uniqueCount, "All message codes must be unique");
    }

    @Test
    void messageCodesShouldFollowNamingConvention() {
        List<String> codes = getPublicStringConstants(MessageCatalog.class);

        for (String code : codes) {
            assertTrue(code.matches("^[A-Z]+_\\d{3}$"), 
                "Message code '" + code + "' should follow format MODULE_NNN");
        }
    }

    @Test
    void getMessageShouldReturnMessageForKnownCode() {
        String message = MessageCatalog.getMessage(MessageCatalog.ACCOUNT_NOT_FOUND);
        assertNotNull(message);
        assertFalse(message.isEmpty());
        assertNotEquals(MessageCatalog.ACCOUNT_NOT_FOUND, message);
    }

    @Test
    void getMessageShouldReturnDefaultForUnknownCode() {
        String defaultMessage = "Default message";
        String result = MessageCatalog.getMessage("UNKNOWN_CODE", defaultMessage);
        assertEquals(defaultMessage, result);
    }

    private List<String> getPublicStringConstants(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(f -> Modifier.isPublic(f.getModifiers()))
            .filter(f -> f.getType().equals(String.class))
            .map(f -> {
                try {
                    return (String) f.get(null);
                } catch (IllegalAccessException e) {
                    return null;
                }
            })
            .filter(s -> s != null)
            .collect(Collectors.toList());
    }
}
