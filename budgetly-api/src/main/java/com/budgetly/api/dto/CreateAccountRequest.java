package com.budgetly.api.dto;

import com.budgetly.persistence.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "Account name is required") String name,
        @NotNull(message = "Account type is required") AccountType type,
        @NotNull(message = "Balance is required") BigDecimal balance,
        @NotBlank(message = "Currency is required") String currency) {}
