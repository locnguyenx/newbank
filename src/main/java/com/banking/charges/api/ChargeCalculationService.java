package com.banking.charges.api;

import com.banking.charges.api.dto.ChargeResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * Public API for charge calculation.
 * Other modules should depend on this interface, not on internal implementations.
 */
public interface ChargeCalculationService {

    /**
     * Calculates a single charge by type for a product.
     *
     * @param productId the ID of the product
     * @param chargeType the type of charge to calculate
     * @param amount the transaction amount
     * @param currency the currency code
     * @return the calculated charge result
     */
    ChargeResult calculateCharge(Long productId, String chargeType, BigDecimal amount, String currency);

    /**
     * Calculates all applicable charges for a product.
     *
     * @param productId the ID of the product
     * @param amount the transaction amount
     * @param currency the currency code
     * @return list of all calculated charge results
     */
    List<ChargeResult> calculateAllCharges(Long productId, BigDecimal amount, String currency);
}
