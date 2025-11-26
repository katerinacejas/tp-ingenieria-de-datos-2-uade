package com.poliglota.controller;

import com.poliglota.model.mysql.Account;
import com.poliglota.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.poliglota.DTO.AccountDTO;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    //  Obtener todas las cuentas
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    //  Obtener una cuenta por ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(  Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    //  Obtener una cuenta por ID de usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<AccountDTO> getAccountByUser(  Long userId) {
        return ResponseEntity.ok(accountService.getAccountByUserId(userId));
    }

    //  Acreditar saldo
    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(
              Long id,
               double amount) {
        return ResponseEntity.ok(accountService.deposit(id, amount));
    }

    //  Debitar saldo
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(
              Long id,
               double amount) {
        return ResponseEntity.ok(accountService.withdraw(id, amount));
    }

}
