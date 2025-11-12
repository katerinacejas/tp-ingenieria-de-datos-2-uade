package com.poliglota.service;

import com.poliglota.model.mysql.Account;
import com.poliglota.model.mysql.AccountMovementHistory;
import com.poliglota.DTO.AccountDTO;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.AccountRepository;
import com.poliglota.repository.UserRepository;
import com.poliglota.repository.AccountMovementHistoryRepository;
import lombok.RequiredArgsConstructor;
import com.poliglota.exception.AccountNotFoundException;
import com.poliglota.exception.UsuarioNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
	private final AccountMovementHistoryRepository accountMovementHistoryRepository;

    //  Obtener todas las cuentas
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()			
			.stream()
			.map(account -> this.toDto(account))
			.toList();
    }

    //  Obtener cuenta por ID
    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id)
			.map(this::toDto)
			.orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con ID: " + id));
    }

    //  Obtener cuenta por usuario
    public AccountDTO getAccountByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + userId));
        return toDto(accountRepository.findByUserId(user.getUserId()));
    }

    //  Acreditar saldo
    public AccountDTO deposit(Long accountId, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new AccountNotFoundException("Cuenta  no encontrada con ID: " + accountId));
        
		AccountMovementHistory accountMovementHistory = new AccountMovementHistory();
		accountMovementHistory.setAccount(account);
		accountMovementHistory.setAmount(amount);
		accountMovementHistory.setMovementType("DEPOSIT");
		accountMovementHistory.setBalanceAfterMovement(account.getCurrentBalance() + amount);
		accountMovementHistory.setBalanceBeforeMovement(account.getCurrentBalance());
		accountMovementHistory.setMovementDate(LocalDateTime.now());
		accountMovementHistoryRepository.save(accountMovementHistory);

		account.setCurrentBalance(account.getCurrentBalance() + amount);

        return toDto(accountRepository.save(account));
    }

    //  Debitar saldo
    public Account withdraw(Long accountId, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto debe ser mayor que cero.");
      
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new AccountNotFoundException("Cuenta  no encontrada con ID: " + accountId));
      
        if (account.getCurrentBalance() < amount)
            throw new IllegalStateException("Fondos insuficientes en la cuenta.");

		AccountMovementHistory accountMovementHistory = new AccountMovementHistory();
		accountMovementHistory.setAccount(account);
		accountMovementHistory.setAmount(amount);
		accountMovementHistory.setMovementType("WITHDRAW");
		accountMovementHistory.setBalanceAfterMovement(account.getCurrentBalance() - amount);
		accountMovementHistory.setBalanceBeforeMovement(account.getCurrentBalance());
		accountMovementHistory.setMovementDate(LocalDateTime.now());
		accountMovementHistoryRepository.save(accountMovementHistory);
		
        account.setCurrentBalance(account.getCurrentBalance() - amount);

        return accountRepository.save(account);
    }

	private AccountDTO toDto(Account account) {
		AccountDTO dto = new AccountDTO();
		dto.setAccountId(account.getAccountId().toString());
		dto.setUserId(account.getUserId().getUserId().toString());
		dto.setCurrentBalance(account.getCurrentBalance());
		return dto;
	}
}
