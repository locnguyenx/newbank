package com.banking.account.controller;

import com.banking.account.service.AccountService;
import com.banking.account.dto.AccountHolderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountNumber}/holders")
public class AccountHolderController {

    private final AccountService accountService;

    public AccountHolderController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Void> addHolder(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountHolderRequest request) {
        accountService.addAccountHolder(accountNumber, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{holderId}")
    public ResponseEntity<Void> removeHolder(
            @PathVariable String accountNumber,
            @PathVariable Long holderId) {
        accountService.removeAccountHolder(holderId);
        return ResponseEntity.noContent().build();
    }
}
