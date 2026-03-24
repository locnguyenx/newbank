package com.banking.cashmanagement.controller;

import com.banking.cashmanagement.dto.LiquidityPositionResponse;
import com.banking.cashmanagement.service.LiquidityService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> getCurrentPosition(@RequestParam Long customerId) {
        LiquidityPositionResponse response = liquidityService.getCurrentPosition(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/position/history")
    public ResponseEntity<Map<String, Object>> getPositionHistory(@RequestParam Long customerId) {
        List<LiquidityPositionResponse> positions = liquidityService.getPositionHistory(customerId);
        return ResponseEntity.ok(Map.of("success", true, "data", positions));
    }
}
