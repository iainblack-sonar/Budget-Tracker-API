package com.budgetly.common.exception;

public class ResourceNotFoundException extends BudgetlyException {

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
