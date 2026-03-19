package com.banking.customer.domain.embeddable;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Money money1 = new Money(new BigDecimal("10.50"), "USD");
        Money money2 = new Money(new BigDecimal("5.25"), "USD");

        Money result = money1.add(money2);

        assertEquals(new BigDecimal("15.75"), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void shouldSubtractMoneyWithSameCurrency() {
        Money money1 = new Money(new BigDecimal("20.00"), "USD");
        Money money2 = new Money(new BigDecimal("7.50"), "USD");

        Money result = money1.subtract(money2);

        assertEquals(new BigDecimal("12.50"), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void shouldMultiplyMoney() {
        Money money = new Money(new BigDecimal("10.00"), "USD");

        Money result = money.multiply(new BigDecimal("3"));

        assertEquals(new BigDecimal("30.00"), result.getAmount());
    }

    @Test
    void shouldDivideMoney() {
        Money money = new Money(new BigDecimal("100.00"), "USD");

        Money result = money.divide(new BigDecimal("4"));

        assertEquals(new BigDecimal("25.00"), result.getAmount());
    }

    @Test
    void shouldApplyBankersRoundingForHalfEven() {
        Money money = new Money(new BigDecimal("10.555"), "USD");

        assertEquals(new BigDecimal("10.56"), money.getAmount());
    }

    @Test
    void shouldApplyBankersRoundingDownForExactHalf() {
        Money money = new Money(new BigDecimal("10.565"), "USD");

        assertEquals(new BigDecimal("10.56"), money.getAmount());
    }

    @Test
    void shouldApplyBankersRoundingUpForOddHalf() {
        Money money = new Money(new BigDecimal("10.575"), "USD");

        assertEquals(new BigDecimal("10.58"), money.getAmount());
    }

    @Test
    void shouldThrowExceptionForCurrencyMismatch() {
        Money money1 = new Money(new BigDecimal("10.00"), "USD");
        Money money2 = new Money(new BigDecimal("5.00"), "EUR");

        assertThrows(IllegalArgumentException.class, () -> money1.add(money2));
    }

    @Test
    void shouldHandleZeroAmount() {
        Money money = new Money(BigDecimal.ZERO, "USD");

        Money result = money.add(new Money(new BigDecimal("10.00"), "USD"));

        assertEquals(new BigDecimal("10.00"), result.getAmount());
    }

    @Test
    void shouldPreserveCurrencyOnMultiply() {
        Money money = new Money(new BigDecimal("10.00"), "EUR");

        Money result = money.multiply(new BigDecimal("2"));

        assertEquals(new BigDecimal("20.00"), result.getAmount());
        assertEquals("EUR", result.getCurrency());
    }
}
