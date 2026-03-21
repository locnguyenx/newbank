package com.banking.account.service;

import com.banking.account.dto.AccountResponse;
import com.banking.account.dto.CustomerAccountSummary;
import com.banking.account.mapper.AccountMapper;
import com.banking.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public CustomerAccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public List<AccountResponse> getCustomerAccounts(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
            .map(accountMapper::toResponse)
            .collect(Collectors.toList());
    }

    public CustomerAccountSummary getCustomerAccountSummary(Long customerId) {
        List<AccountResponse> accounts = getCustomerAccounts(customerId);
        
        BigDecimal totalBalance = accounts.stream()
            .map(AccountResponse::getBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Determine common currency - for simplicity assume all same currency
        String currency = accounts.isEmpty() ? "USD" : accounts.get(0).getCurrency();
        
        return new CustomerAccountSummary(customerId, totalBalance, accounts.size(), currency);
    }
}
