package com.banking.charges.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class CreateInterestRateRequest {

    private Long chargeId;
    private String productCode;
    private BigDecimal fixedRate;
    private String accrualSchedule;
    private String applicationSchedule;
    private List<InterestTierRequest> tiers;

    public Long getChargeId() {
        return chargeId;
    }

    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public String getAccrualSchedule() {
        return accrualSchedule;
    }

    public void setAccrualSchedule(String accrualSchedule) {
        this.accrualSchedule = accrualSchedule;
    }

    public String getApplicationSchedule() {
        return applicationSchedule;
    }

    public void setApplicationSchedule(String applicationSchedule) {
        this.applicationSchedule = applicationSchedule;
    }

    public List<InterestTierRequest> getTiers() {
        return tiers;
    }

    public void setTiers(List<InterestTierRequest> tiers) {
        this.tiers = tiers;
    }
}
