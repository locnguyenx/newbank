package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.LiquidityPositionResponse;
import com.banking.cashmanagement.service.LiquidityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cash-management/liquidity")
public class LiquidityController {
    
    private final LiquidityService liquidityService;
    
    public LiquidityController(LiquidityService liquidityService) {
        this.liquidityService = liquidityService;
    }
    
    @GetMapping("/position")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> getCurrentPosition(@RequestParam Long customerId) {
        LiquidityPositionResponse response = liquidityService.getCurrentPosition(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/position/history")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_MAKER', 'COMPANY_CHECKER', 'COMPANY_VIEWER', 'DEPARTMENT_MAKER', 'DEPARTMENT_CHECKER', 'DEPARTMENT_VIEWER')")
    public ResponseEntity<Map<String, Object>> getPositionHistory(@RequestParam Long customerId) {
        List<LiquidityPositionResponse> positions = liquidityService.getPositionHistory(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", positions));
    }
}
