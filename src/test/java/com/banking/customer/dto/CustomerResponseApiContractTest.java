package com.banking.customer.dto;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Contract tests that verify CustomerResponse DTO has the correct field names
 * that the frontend expects.
 *
 * This catches mismatches like the annualRevenue vs annualRevenueAmount issue.
 */
class CustomerResponseApiContractTest {

    /**
     * Verify CustomerResponse class has getAnnualRevenueAmount() and getAnnualRevenueCurrency().
     * These are the SPLIT money fields that the frontend needs.
     */
    @Test
    void customerResponse_hasSplitMoneyFields() throws Exception {
        // Given - Corporate customer response class
        Class<?> clazz = CustomerResponse.class;

        // When - Look for the getter methods
        Method getAmount = null;
        Method getCurrency = null;
        try {
            getAmount = clazz.getMethod("getAnnualRevenueAmount");
            getCurrency = clazz.getMethod("getAnnualRevenueCurrency");
        } catch (NoSuchMethodException e) {
            // Methods don't exist - fail the test
        }

        // Then - Assert both methods exist and return correct types
        assertThat(getAmount)
                .as("CustomerResponse should have getAnnualRevenueAmount() method")
                .isNotNull();
        assertThat(getAmount.getReturnType())
                .as("getAnnualRevenueAmount() should return BigDecimal")
                .isAssignableFrom(java.math.BigDecimal.class);

        assertThat(getCurrency)
                .as("CustomerResponse should have getAnnualRevenueCurrency() method")
                .isNotNull();
        assertThat(getCurrency.getReturnType())
                .as("getAnnualRevenueCurrency() should return String")
                .isAssignableFrom(String.class);
    }

    /**
     * Verify that CustomerResponse does NOT have getAnnualRevenue() method.
     * This prevents accidental reintroduction of the wrong field name.
     */
    @Test
    void customerResponse_doesNotHaveCombinedAnnualRevenueField() throws Exception {
        // Given
        Class<?> clazz = CustomerResponse.class;

        // When - try to find the combined field (should NOT exist)
        boolean hasAnnualRevenue = false;
        boolean hasRevenueCurrency = false;
        try {
            clazz.getMethod("getAnnualRevenue");
            hasAnnualRevenue = true;
        } catch (NoSuchMethodException ignored) {
        }
        try {
            clazz.getMethod("getRevenueCurrency");
            hasRevenueCurrency = true;
        } catch (NoSuchMethodException ignored) {
        }

        // Then - Assert these WRONG methods do not exist
        assertThat(hasAnnualRevenue)
                .as("CustomerResponse should NOT have getAnnualRevenue() method - use getAnnualRevenueAmount() instead")
                .isFalse();
        assertThat(hasRevenueCurrency)
                .as("CustomerResponse should NOT have getRevenueCurrency() method - use getAnnualRevenueCurrency() instead")
                .isFalse();
    }

    /**
     * Verify that for SME customers, turnover fields exist (not revenue).
     */
    @Test
    void customerResponse_hasTurnoverFieldsForSME() throws Exception {
        // Given
        Class<?> clazz = CustomerResponse.class;

        // When
        Method getTurnoverAmount = null;
        Method getTurnoverCurrency = null;
        try {
            getTurnoverAmount = clazz.getMethod("getAnnualTurnoverAmount");
            getTurnoverCurrency = clazz.getMethod("getAnnualTurnoverCurrency");
        } catch (NoSuchMethodException e) {
            // Methods don't exist - fail the test
        }

        // Then
        assertThat(getTurnoverAmount)
                .as("CustomerResponse should have getAnnualTurnoverAmount() method for SME customers")
                .isNotNull();
        assertThat(getTurnoverCurrency)
                .as("CustomerResponse should have getAnnualTurnoverCurrency() method for SME customers")
                .isNotNull();
        assertThat(getTurnoverAmount.getReturnType())
                .isAssignableFrom(java.math.BigDecimal.class);
        assertThat(getTurnoverCurrency.getReturnType())
                .isAssignableFrom(String.class);
    }
}
