package com.banking.account.mapper;

import com.banking.account.domain.entity.Account;
import com.banking.account.dto.AccountResponse;
import com.banking.account.domain.enums.AccountType;
import com.banking.account.domain.enums.AccountStatus;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
            account.getId(),
            account.getAccountNumber(),
            account.getType(),
            account.getStatus(),
            account.getCurrency(),
            account.getBalance(),
            account.getProductId(),
            account.getProductVersionId(),
            account.getProductName(),
            account.getCustomerId(),
            account.getOpenedAt(),
            account.getClosedAt()
        );
    }
}
