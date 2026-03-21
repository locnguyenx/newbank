package com.banking.limits.controller;

import com.banking.limits.dto.request.ApprovalActionRequest;
import com.banking.limits.dto.response.ApprovalRequestResponse;
import com.banking.limits.service.ApprovalService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/limits/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ApprovalRequestResponse>> getPendingApprovals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<ApprovalRequestResponse> response = approvalService.getPendingApprovals(PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id,
                                        @RequestParam String username) {
        approvalService.approve(id, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id,
                                       @RequestParam String username,
                                       @RequestBody ApprovalActionRequest request) {
        approvalService.reject(id, username, request.getReason());
        return ResponseEntity.ok().build();
    }
}
