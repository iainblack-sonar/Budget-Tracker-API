package com.budgetly.common.exception;

public class ValidationException extends BudgetlyException {

    public ValidationException(String message) {
        super(message, 400);
    }
}
