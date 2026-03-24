package com.banking.cashmanagement.integration;

import com.banking.account.api.AccountQueryService;
import com.banking.account.api.dto.AccountDTO;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Optional;

@Component
public class AccountServiceAdapter {
    
    private final AccountQueryService accountQueryService;
    
    public AccountServiceAdapter(AccountQueryService accountQueryService) {
        this.accountQueryService = accountQueryService;
    }
    
    public BigDecimal getAvailableBalance(Long accountId) {
        AccountDTO account = accountQueryService.findById(accountId);
        return account != null ? account.getAvailableBalance() : null;
    }
    
    public boolean accountExists(Long accountId) {
        return accountQueryService.existsById(accountId);
    }
    
    public Optional<AccountDTO> getAccount(Long accountId) {
        return Optional.ofNullable(accountQueryService.findById(accountId));
    }
}
