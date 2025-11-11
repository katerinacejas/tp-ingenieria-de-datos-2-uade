package com.poliglota.controller;

import com.poliglota.model.mysql.Account;
import com.poliglota.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 🔹 Obtener todas las cuentas
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // 🔹 Obtener una cuenta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    // 🔹 Obtener una cuenta por ID de usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<Account> getAccountByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountByUserId(userId));
    }

    // 🔹 Crear una nueva cuenta
    @PostMapping("/user/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.createAccount(userId));
    }

    // 🔹 Acreditar saldo
    @PutMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable Long id,
            @RequestParam double amount) {
        return ResponseEntity.ok(accountService.deposit(id, amount));
    }

    // 🔹 Debitar saldo
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long id,
            @RequestParam double amount) {
        return ResponseEntity.ok(accountService.withdraw(id, amount));
    }

    // 🔹 Eliminar una cuenta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
