package com.banking.customer.repository;

import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.enums.KYCStatus;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KYCCheckRepositoryTest {

    @Test
    void shouldHaveFindByCustomerAndStatusMethod() throws NoSuchMethodException {
        Method method = KYCCheckRepository.class.getMethod("findByCustomerAndStatus", Customer.class, KYCStatus.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    void shouldHaveFindByDueDateBeforeMethod() throws NoSuchMethodException {
        Method method = KYCCheckRepository.class.getMethod("findByDueDateBefore", Instant.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    void shouldBeARepository() {
        assertTrue(KYCCheckRepository.class.isInterface());
    }
}