package com.budgetly.core;

import com.budgetly.common.exception.ResourceNotFoundException;
import com.budgetly.common.exception.ValidationException;
import com.budgetly.persistence.entity.AccountEntity;
import com.budgetly.persistence.entity.AccountType;
import com.budgetly.persistence.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountEntity createAccount(
            Long userId, String name, AccountType type, BigDecimal balance, String currency) {
        validateBalance(type, balance);

        AccountEntity account =
                AccountEntity.builder()
                        .userId(userId)
                        .name(name)
                        .type(type)
                        .balance(balance)
                        .currency(currency)
                        .build();

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public AccountEntity getAccountById(Long userId, Long accountId) {
        return accountRepository
                .findByIdAndUserId(accountId, userId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Account not found with id: " + accountId));
    }

    @Transactional(readOnly = true)
    public List<AccountEntity> getAllAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    @Transactional
    public AccountEntity updateAccount(
            Long userId,
            Long accountId,
            String name,
            AccountType type,
            BigDecimal balance,
            String currency) {
        AccountEntity account = getAccountById(userId, accountId);

        if (name != null) {
            account.setName(name);
        }
        if (type != null) {
            account.setType(type);
        }
        if (balance != null) {
            validateBalance(type != null ? type : account.getType(), balance);
            account.setBalance(balance);
        }
        if (currency != null) {
            account.setCurrency(currency);
        }

        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long userId, Long accountId) {
        AccountEntity account = getAccountById(userId, accountId);
        accountRepository.delete(account);
    }

    private void validateBalance(AccountType type, BigDecimal balance) {
        if (type != AccountType.CREDIT_CARD && balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Balance cannot be negative for " + type + " accounts");
        }
    }
}
