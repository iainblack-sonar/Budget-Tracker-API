package com.budgetly.api.controller;

import com.budgetly.api.dto.AccountResponse;
import com.budgetly.api.dto.CreateAccountRequest;
import com.budgetly.api.dto.UpdateAccountRequest;
import com.budgetly.auth.AuthenticatedUser;
import com.budgetly.core.AccountService;
import com.budgetly.persistence.entity.AccountEntity;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreateAccountRequest request) {
        AccountEntity account =
                accountService.createAccount(
                        user.userId(),
                        request.name(),
                        request.type(),
                        request.balance(),
                        request.currency());
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(
            @AuthenticationPrincipal AuthenticatedUser user) {
        List<AccountResponse> accounts =
                accountService.getAllAccounts(user.userId()).stream()
                        .map(AccountResponse::from)
                        .toList();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long id) {
        AccountEntity account = accountService.getAccountById(user.userId(), id);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request) {
        AccountEntity account =
                accountService.updateAccount(
                        user.userId(),
                        id,
                        request.name(),
                        request.type(),
                        request.balance(),
                        request.currency());
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long id) {
        accountService.deleteAccount(user.userId(), id);
        return ResponseEntity.noContent().build();
    }
}
