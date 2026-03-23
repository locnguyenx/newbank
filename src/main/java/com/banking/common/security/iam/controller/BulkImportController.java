package com.banking.common.security.iam.controller;

import com.banking.common.security.iam.dto.BulkImportRequest;
import com.banking.common.security.iam.dto.BulkImportResult;
import com.banking.common.security.iam.service.BulkImportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/iam/bulk-import")
@PreAuthorize("hasRole('SYSTEM_ADMIN')")
public class BulkImportController {

    private final BulkImportService bulkImportService;

    public BulkImportController(BulkImportService bulkImportService) {
        this.bulkImportService = bulkImportService;
    }

    @PostMapping("/users")
    public ResponseEntity<BulkImportResult> importUsers(@Valid @RequestBody BulkImportRequest request) {
        BulkImportResult result = bulkImportService.importUsers(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}