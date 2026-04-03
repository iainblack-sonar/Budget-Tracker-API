package com.budgetly.api.dto;

import com.budgetly.persistence.entity.AccountEntity;
import com.budgetly.persistence.entity.AccountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String name,
        AccountType type,
        BigDecimal balance,
        String currency,
        LocalDateTime createdAt) {

    public static AccountResponse from(AccountEntity entity) {
        return new AccountResponse(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getBalance(),
                entity.getCurrency(),
                entity.getCreatedAt());
    }
}
