package com.budgetly.api.dto;

import com.budgetly.persistence.entity.AccountType;
import java.math.BigDecimal;

public record UpdateAccountRequest(
        String name, AccountType type, BigDecimal balance, String currency) {}
