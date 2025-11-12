package com.poliglota.service;

import com.poliglota.model.mysql.Account;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.AccountRepository;
import com.poliglota.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    // 🔹 Obtener todas las cuentas
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // 🔹 Obtener cuenta por ID
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + id));
    }

    // 🔹 Obtener cuenta por usuario
    public Account getAccountByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
        return accountRepository.findByUserId(user.getId());
    }

    // 🔹 Crear cuenta nueva para un usuario
    public Account createAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        if (accountRepository.findByUserId(user.getId()) != null) {
            throw new IllegalStateException("El usuario ya tiene una cuenta asociada.");
        }

        Account account = new Account();
        account.setUserId(user);
        account.setCurrentBalance(0.0);

        return accountRepository.save(account);
    }

    // 🔹 Acreditar saldo
    public Account deposit(Long accountId, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        Account account = getAccountById(accountId);
        account.setCurrentBalance(account.getCurrentBalance() + amount);
        return accountRepository.save(account);
    }

    // 🔹 Debitar saldo
    public Account withdraw(Long accountId, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        Account account = getAccountById(accountId);

        if (account.getCurrentBalance() < amount)
            throw new IllegalStateException("Fondos insuficientes en la cuenta.");

        account.setCurrentBalance(account.getCurrentBalance() - amount);
        return accountRepository.save(account);
    }

    // 🔹 Eliminar cuenta
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
