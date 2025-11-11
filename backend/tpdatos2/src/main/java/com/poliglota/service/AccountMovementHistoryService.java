package com.poliglota.service.mysql;

import com.poliglota.model.mysql.Account;
import com.poliglota.model.mysql.AccountMovementHistory;
import com.poliglota.repository.mysql.AccountMovementHistoryRepository;
import com.poliglota.repository.mysql.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountMovementHistoryService {

    private final AccountMovementHistoryRepository movementHistoryRepository;
    private final AccountRepository accountRepository;

    // 🔹 Obtener todos los movimientos
    public List<AccountMovementHistory> getAllMovements() {
        return movementHistoryRepository.findAll();
    }

    // 🔹 Obtener movimiento por ID
    public AccountMovementHistory getMovementById(Long id) {
        return movementHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con ID: " + id));
    }

    // 🔹 Movimientos por cuenta
    public List<AccountMovementHistory> getMovementsByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + accountId));
        return movementHistoryRepository.findByAccount(account);
    }

    // 🔹 Movimientos entre fechas
    public List<AccountMovementHistory> getMovementsByDateRange(LocalDateTime start, LocalDateTime end) {
        return movementHistoryRepository.findByMovementDateBetween(start, end);
    }

    // 🔹 Registrar un movimiento (depósito o retiro)
    public AccountMovementHistory registerMovement(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + accountId));

        AccountMovementHistory movement = new AccountMovementHistory();
        movement.setAccount(account);
        movement.setAmount(amount);
        movement.setMovementDate(LocalDateTime.now());

        // 🔸 Actualizamos saldo de la cuenta
        double newBalance = account.getCurrentBalance() + amount.doubleValue();
        account.setCurrentBalance(newBalance);
        accountRepository.save(account);

        return movementHistoryRepository.save(movement);
    }

    // 🔹 Eliminar un movimiento (solo admins normalmente)
    public void deleteMovement(Long id) {
        movementHistoryRepository.deleteById(id);
    }
}
