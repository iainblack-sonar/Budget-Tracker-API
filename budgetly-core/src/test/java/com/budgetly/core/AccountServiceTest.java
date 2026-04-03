package com.budgetly.core;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.budgetly.common.exception.ResourceNotFoundException;
import com.budgetly.common.exception.ValidationException;
import com.budgetly.persistence.entity.AccountEntity;
import com.budgetly.persistence.entity.AccountType;
import com.budgetly.persistence.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository);
    }

    @Test
    void createAccount_validChecking_succeeds() {
        AccountEntity saved =
                AccountEntity.builder()
                        .id(1L)
                        .userId(1L)
                        .name("My Checking")
                        .type(AccountType.CHECKING)
                        .balance(new BigDecimal("1000.00"))
                        .currency("USD")
                        .build();

        when(accountRepository.save(any(AccountEntity.class))).thenReturn(saved);

        AccountEntity result =
                accountService.createAccount(
                        1L, "My Checking", AccountType.CHECKING, new BigDecimal("1000.00"), "USD");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("My Checking");
        verify(accountRepository).save(any(AccountEntity.class));
    }

    @Test
    void createAccount_negativeBalanceChecking_throwsValidation() {
        assertThatThrownBy(
                        () ->
                                accountService.createAccount(
                                        1L,
                                        "Bad Account",
                                        AccountType.CHECKING,
                                        new BigDecimal("-100.00"),
                                        "USD"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("negative");
    }

    @Test
    void createAccount_negativeBalanceSavings_throwsValidation() {
        assertThatThrownBy(
                        () ->
                                accountService.createAccount(
                                        1L,
                                        "Bad Savings",
                                        AccountType.SAVINGS,
                                        new BigDecimal("-50.00"),
                                        "USD"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void createAccount_negativeBalanceCreditCard_succeeds() {
        AccountEntity saved =
                AccountEntity.builder()
                        .id(2L)
                        .userId(1L)
                        .name("My CC")
                        .type(AccountType.CREDIT_CARD)
                        .balance(new BigDecimal("-500.00"))
                        .currency("USD")
                        .build();

        when(accountRepository.save(any(AccountEntity.class))).thenReturn(saved);

        AccountEntity result =
                accountService.createAccount(
                        1L, "My CC", AccountType.CREDIT_CARD, new BigDecimal("-500.00"), "USD");

        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("-500.00"));
    }

    @Test
    void getAccountById_ownedAccount_returnsAccount() {
        AccountEntity account = AccountEntity.builder().id(1L).userId(1L).name("Checking").build();

        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(account));

        AccountEntity result = accountService.getAccountById(1L, 1L);
        assertThat(result.getName()).isEqualTo("Checking");
    }

    @Test
    void getAccountById_otherUsersAccount_throwsNotFound() {
        when(accountRepository.findByIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(2L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllAccounts_returnsList() {
        List<AccountEntity> accounts =
                List.of(
                        AccountEntity.builder().id(1L).userId(1L).name("A").build(),
                        AccountEntity.builder().id(2L).userId(1L).name("B").build());

        when(accountRepository.findAllByUserId(1L)).thenReturn(accounts);

        List<AccountEntity> result = accountService.getAllAccounts(1L);
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteAccount_ownedAccount_succeeds() {
        AccountEntity account = AccountEntity.builder().id(1L).userId(1L).name("Checking").build();

        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L, 1L);
        verify(accountRepository).delete(account);
    }
}
