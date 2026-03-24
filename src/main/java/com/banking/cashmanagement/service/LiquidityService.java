package com.banking.cashmanagement.service;

import com.banking.cashmanagement.domain.entity.LiquidityPosition;
import com.banking.cashmanagement.dto.LiquidityPositionResponse;
import com.banking.cashmanagement.repository.LiquidityPositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LiquidityService {
    
    private final LiquidityPositionRepository liquidityPositionRepository;
    
    public LiquidityService(LiquidityPositionRepository liquidityPositionRepository) {
        this.liquidityPositionRepository = liquidityPositionRepository;
    }
    
    @Transactional(readOnly = true)
    public LiquidityPositionResponse getCurrentPosition(Long customerId) {
        return liquidityPositionRepository.findFirstByCustomerIdOrderByCalculationDateTimeDesc(customerId)
            .map(this::mapToResponse)
            .orElse(null);
    }
    
    @Transactional(readOnly = true)
    public List<LiquidityPositionResponse> getPositionHistory(Long customerId) {
        return liquidityPositionRepository.findByCustomerId(customerId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<LiquidityPositionResponse> getPositionByCurrency(Long customerId, String currency) {
        return liquidityPositionRepository.findByCustomerIdAndCurrency(customerId, currency).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    private LiquidityPositionResponse mapToResponse(LiquidityPosition position) {
        LiquidityPositionResponse response = new LiquidityPositionResponse();
        response.setId(position.getId());
        response.setCustomerId(position.getCustomerId());
        response.setCalculationDateTime(position.getCalculationDateTime());
        response.setTotalPosition(position.getTotalPosition());
        response.setCurrency(position.getCurrency());
        response.setAccountBreakdown(position.getAccountBreakdown());
        response.setAvailableLiquidity(position.getAvailableLiquidity());
        response.setProjectedLiquidity(position.getProjectedLiquidity());
        return response;
    }
}
