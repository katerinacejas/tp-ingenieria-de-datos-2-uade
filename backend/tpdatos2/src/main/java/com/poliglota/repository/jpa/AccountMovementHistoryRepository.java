package com.poliglota.repository.jpa;

import com.poliglota.model.mysql.Account;
import com.poliglota.model.mysql.AccountMovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountMovementHistoryRepository extends JpaRepository<AccountMovementHistory, Long> {

    List<AccountMovementHistory> findByAccount(Account account);

    List<AccountMovementHistory> findByMovementDateBetween(LocalDateTime start, LocalDateTime end);
}
