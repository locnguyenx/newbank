package com.banking.customer.controller;

import com.banking.customer.dto.AuthorizationResponse;
import com.banking.customer.dto.CreateAuthorizationRequest;
import com.banking.customer.dto.UpdateAuthorizationRequest;
import com.banking.customer.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authorizations")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity<AuthorizationResponse> createAuthorization(
            @RequestParam Long customerId,
            @Valid @RequestBody CreateAuthorizationRequest request) {
        
        AuthorizationResponse response = authorizationService.createAuthorization(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AuthorizationResponse>> getAuthorizationsByCustomer(@PathVariable Long customerId) {
        List<AuthorizationResponse> responses = authorizationService.getActiveAuthorizations(customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorizationResponse> getAuthorizationById(@PathVariable Long id) {
        List<AuthorizationResponse> authorizations = authorizationService.getActiveAuthorizations(null);
        AuthorizationResponse response = authorizations.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Authorization not found: " + id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorizationResponse> updateAuthorization(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAuthorizationRequest request) {
        
        AuthorizationResponse response = authorizationService.updateAuthorization(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/revoke")
    public ResponseEntity<Void> revokeAuthorization(@PathVariable Long id) {
        authorizationService.revokeAuthorization(id);
        return ResponseEntity.noContent().build();
    }
}
