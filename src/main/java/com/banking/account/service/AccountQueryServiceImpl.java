package com.banking.account.service;

import com.banking.account.api.AccountQueryService;
import com.banking.account.api.dto.AccountDTO;
import com.banking.account.domain.entity.Account;
import com.banking.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountRepository accountRepository;

    public AccountQueryServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTO findById(Long id) {
        return accountRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return accountRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> findByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private AccountDTO toDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getType() != null ? account.getType().name() : null);
        dto.setStatus(account.getStatus() != null ? account.getStatus().name() : null);
        dto.setAvailableBalance(account.getBalance());
        dto.setCurrencyCode(account.getCurrency());
        dto.setCustomerId(account.getCustomerId());
        return dto;
    }
}
