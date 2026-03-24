package com.banking.cashmanagement.integration;

import com.banking.charges.api.ChargeCalculationService;
import com.banking.charges.api.dto.ChargeResult;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
public class ChargesServiceAdapter {
    
    private final ChargeCalculationService chargeCalculationService;
    
    public ChargesServiceAdapter(ChargeCalculationService chargeCalculationService) {
        this.chargeCalculationService = chargeCalculationService;
    }
    
    public ChargeResult calculateCharge(Long productId, String chargeType, BigDecimal amount, String currency) {
        return chargeCalculationService.calculateCharge(productId, chargeType, amount, currency);
    }
    
    public List<ChargeResult> calculateAllCharges(Long productId, BigDecimal amount, String currency) {
        return chargeCalculationService.calculateAllCharges(productId, amount, currency);
    }
}
