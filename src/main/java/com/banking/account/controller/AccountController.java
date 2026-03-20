package com.banking.account.controller;

import com.banking.account.service.AccountService;
import com.banking.account.service.AccountInquiryService;
import com.banking.account.dto.AccountOpeningRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.domain.embeddable.AccountBalance;
import com.banking.account.dto.AccountStatement;
import com.banking.account.dto.AccountStatementFilter;
import com.banking.account.domain.enums.AccountStatus;
import com.banking.account.domain.enums.AccountType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountInquiryService inquiryService;

    public AccountController(AccountService accountService, AccountInquiryService inquiryService) {
        this.accountService = accountService;
        this.inquiryService = inquiryService;
    }

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAccounts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) AccountType type,
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AccountResponse> accounts = accountService.getAccounts(search, type, status, customerId, PageRequest.of(page, size));
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> openAccount(@Valid @RequestBody AccountOpeningRequest request) {
        AccountResponse response = accountService.openAccount(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccountDetails(@PathVariable String accountNumber) {
        // Use inquiry service to get details (which includes holders)
        // The inquiry service returns a different DTO (AccountDetails), but we can return it as a generic response
        return ResponseEntity.ok(inquiryService.getAccountDetails(accountNumber));
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<AccountBalance> getAccountBalance(@PathVariable String accountNumber) {
        AccountBalance balance = inquiryService.getAccountBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<AccountStatement> getAccountStatement(
            @PathVariable String accountNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        AccountStatementFilter filter = new AccountStatementFilter();
        if (fromDate != null) filter.setFromDate(fromDate);
        if (toDate != null) filter.setToDate(toDate);
        AccountStatement statement = inquiryService.getAccountStatement(accountNumber, filter, org.springframework.data.domain.Pageable.unpaged());
        return ResponseEntity.ok(statement);
    }

    @PutMapping("/{accountNumber}/close")
    public ResponseEntity<Void> closeAccount(@PathVariable String accountNumber) {
        accountService.closeAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountNumber}/freeze")
    public ResponseEntity<Void> freezeAccount(@PathVariable String accountNumber) {
        accountService.freezeAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountNumber}/unfreeze")
    public ResponseEntity<Void> unfreezeAccount(@PathVariable String accountNumber) {
        accountService.unfreezeAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }
}
