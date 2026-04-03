package com.budgetly.common.exception;

public class UnauthorizedException extends BudgetlyException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }
}
