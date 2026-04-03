package com.budgetly.persistence.repository;

import com.budgetly.persistence.entity.AccountEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findAllByUserId(Long userId);

    Optional<AccountEntity> findByIdAndUserId(Long id, Long userId);
}
