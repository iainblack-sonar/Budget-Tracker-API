package com.budgetly.common.exception;

import lombok.Getter;

@Getter
public class BudgetlyException extends RuntimeException {

    private final int statusCode;

    public BudgetlyException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public BudgetlyException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
