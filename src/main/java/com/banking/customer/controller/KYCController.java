package com.banking.customer.controller;

import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.KYCCheck;
import com.banking.customer.domain.entity.KYCDocument;
import com.banking.customer.domain.enums.KYCLevel;
import com.banking.customer.domain.enums.KYCDocumentType;
import com.banking.customer.dto.BulkUploadResult;
import com.banking.customer.exception.CustomerNotFoundException;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.service.KYCService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kyc")
public class KYCController {

    private final KYCService kycService;
    private final CustomerRepository customerRepository;

    public KYCController(KYCService kycService, CustomerRepository customerRepository) {
        this.kycService = kycService;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<KYCResponse> initiateKYC(@Valid @RequestBody InitiateKYCRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        KYCCheck kycCheck = kycService.initiateKYC(customer, request.getLevel());

        KYCResponse response = toKYCResponse(kycCheck);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<KYCResponse> submitDocuments(
            @PathVariable Long id,
            @Valid @RequestBody SubmitDocumentsRequest request) {
        
        KYCDocument document = kycService.submitDocuments(
                id, request.getDocumentTypes(), request.getDocumentReferences());

        KYCCheck kycCheck = kycService.getKYCCheckById(id);
        KYCResponse response = toKYCResponse(kycCheck);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<KYCResponse> submitForReview(@PathVariable Long id) {
        KYCCheck kycCheck = kycService.submitForReview(id);
        KYCResponse response = toKYCResponse(kycCheck);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<KYCResponse> approveKYC(
            @PathVariable Long id,
            @RequestBody ApproveKYCRequest request) {
        
        KYCCheck kycCheck = kycService.approveKYC(id, request.getOfficerId());
        KYCResponse response = toKYCResponse(kycCheck);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<KYCResponse> rejectKYC(
            @PathVariable Long id,
            @Valid @RequestBody RejectKYCRequest request) {
        
        KYCCheck kycCheck = kycService.rejectKYC(id, request.getOfficerId(), request.getReason());
        KYCResponse response = toKYCResponse(kycCheck);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KYCResponse> getKYCCheckById(@PathVariable Long id) {
        KYCCheck kycCheck = kycService.getKYCCheckById(id);
        KYCResponse response = toKYCResponse(kycCheck);
        return ResponseEntity.ok(response);
    }

    private KYCResponse toKYCResponse(KYCCheck kycCheck) {
        KYCResponse response = new KYCResponse();
        response.setId(kycCheck.getId());
        response.setCustomerId(kycCheck.getCustomer().getId());
        response.setCustomerName(kycCheck.getCustomer().getName());
        response.setKycLevel(kycCheck.getKycLevel());
        response.setStatus(kycCheck.getStatus());
        response.setAssignedOfficer(kycCheck.getAssignedOfficer());
        response.setRiskScore(kycCheck.getRiskScore());
        response.setDueDate(kycCheck.getDueDate());
        response.setCompletedAt(kycCheck.getCompletedAt());
        response.setNextReviewDate(kycCheck.getNextReviewDate());
        return response;
    }

    public static class InitiateKYCRequest {
        @NotNull(message = "Customer ID is required")
        private Long customerId;

        @NotNull(message = "KYC level is required")
        private KYCLevel level;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public KYCLevel getLevel() {
            return level;
        }

        public void setLevel(KYCLevel level) {
            this.level = level;
        }
    }

    public static class SubmitDocumentsRequest {
        @NotEmpty(message = "Document types are required")
        private List<KYCDocumentType> documentTypes;

        @NotEmpty(message = "Document references are required")
        private List<String> documentReferences;

        public List<KYCDocumentType> getDocumentTypes() {
            return documentTypes;
        }

        public void setDocumentTypes(List<KYCDocumentType> documentTypes) {
            this.documentTypes = documentTypes;
        }

        public List<String> getDocumentReferences() {
            return documentReferences;
        }

        public void setDocumentReferences(List<String> documentReferences) {
            this.documentReferences = documentReferences;
        }
    }

    public static class ApproveKYCRequest {
        @NotNull(message = "Officer ID is required")
        private String officerId;

        public String getOfficerId() {
            return officerId;
        }

        public void setOfficerId(String officerId) {
            this.officerId = officerId;
        }
    }

    public static class RejectKYCRequest {
        @NotNull(message = "Officer ID is required")
        private String officerId;

        @NotNull(message = "Reason is required")
        private String reason;

        public String getOfficerId() {
            return officerId;
        }

        public void setOfficerId(String officerId) {
            this.officerId = officerId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public static class KYCResponse {
        private Long id;
        private Long customerId;
        private String customerName;
        private KYCLevel kycLevel;
        private com.banking.customer.domain.enums.KYCStatus status;
        private String assignedOfficer;
        private Integer riskScore;
        private java.time.Instant dueDate;
        private java.time.Instant completedAt;
        private java.time.Instant nextReviewDate;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public KYCLevel getKycLevel() {
            return kycLevel;
        }

        public void setKycLevel(KYCLevel kycLevel) {
            this.kycLevel = kycLevel;
        }

        public com.banking.customer.domain.enums.KYCStatus getStatus() {
            return status;
        }

        public void setStatus(com.banking.customer.domain.enums.KYCStatus status) {
            this.status = status;
        }

        public String getAssignedOfficer() {
            return assignedOfficer;
        }

        public void setAssignedOfficer(String assignedOfficer) {
            this.assignedOfficer = assignedOfficer;
        }

        public Integer getRiskScore() {
            return riskScore;
        }

        public void setRiskScore(Integer riskScore) {
            this.riskScore = riskScore;
        }

        public java.time.Instant getDueDate() {
            return dueDate;
        }

        public void setDueDate(java.time.Instant dueDate) {
            this.dueDate = dueDate;
        }

        public java.time.Instant getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(java.time.Instant completedAt) {
            this.completedAt = completedAt;
        }

        public java.time.Instant getNextReviewDate() {
            return nextReviewDate;
        }

        public void setNextReviewDate(java.time.Instant nextReviewDate) {
            this.nextReviewDate = nextReviewDate;
        }
    }
}
