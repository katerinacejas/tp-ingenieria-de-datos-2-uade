package com.poliglota.controller;

import com.poliglota.model.mysql.AccountMovementHistory;
import com.poliglota.service.AccountMovementHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/account-movements")
@RequiredArgsConstructor
public class AccountMovementHistoryController {

    private final AccountMovementHistoryService movementHistoryService;

    // 🔹 Obtener todos los movimientos
    @GetMapping
    public ResponseEntity<List<AccountMovementHistory>> getAllMovements() {
        return ResponseEntity.ok(movementHistoryService.getAllMovements());
    }

    // 🔹 Obtener movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountMovementHistory> getMovementById(@PathVariable Long id) {
        return ResponseEntity.ok(movementHistoryService.getMovementById(id));
    }

    // 🔹 Obtener movimientos por cuenta
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<AccountMovementHistory>> getMovementsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(movementHistoryService.getMovementsByAccount(accountId));
    }

    // 🔹 Obtener movimientos entre fechas
    @GetMapping("/between")
    public ResponseEntity<List<AccountMovementHistory>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(movementHistoryService.getMovementsByDateRange(start, end));
    }

    // 🔹 Registrar un movimiento (depósito o retiro)
    @PostMapping("/register")
    public ResponseEntity<AccountMovementHistory> registerMovement(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(movementHistoryService.registerMovement(accountId, amount));
    }

    // 🔹 Eliminar movimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        movementHistoryService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
