package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateBranchRequest;
import com.banking.masterdata.dto.response.BranchResponse;
import com.banking.masterdata.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-data/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    public ResponseEntity<BranchResponse> createBranch(@Valid @RequestBody CreateBranchRequest request) {
        BranchResponse response = branchService.createBranch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getActiveBranches(
            @RequestParam(required = false) String countryCode) {
        List<BranchResponse> response = branchService.getActiveBranches(countryCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<BranchResponse> getBranch(@PathVariable String code) {
        BranchResponse response = branchService.getBranch(code);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}/deactivate")
    public ResponseEntity<BranchResponse> deactivateBranch(@PathVariable String code) {
        BranchResponse response = branchService.deactivateBranch(code);
        return ResponseEntity.ok(response);
    }
}
