package com.poliglota.service;

import com.poliglota.DTO.AccountMovementHistoryDTO;
import com.poliglota.model.mysql.Account;
import com.poliglota.model.mysql.AccountMovementHistory;
import com.poliglota.repository.AccountMovementHistoryRepository;
import com.poliglota.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountMovementHistoryService {

    private final AccountMovementHistoryRepository movementHistoryRepository;
    private final AccountRepository accountRepository;

    //  Obtener todos los movimientos
    public List<AccountMovementHistoryDTO> getAllMovements() {
        return movementHistoryRepository.findAll()
			.stream()
			.map(accountMovementHistory -> this.toDto(accountMovementHistory))
			.toList();
    }

    //  Obtener movimiento por ID
    public AccountMovementHistoryDTO getMovementById(Long id) {
        return movementHistoryRepository.findById(id)
			.map(this::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con ID: " + id));
    }

    //  Movimientos por cuenta
    public List<AccountMovementHistoryDTO> getMovementsByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + accountId));
        return movementHistoryRepository.findByAccount(account)
			.stream()
			.map(accountMovementHistory -> this.toDto(accountMovementHistory))
			.toList();
    }

    //  Movimientos entre fechas
    public List<AccountMovementHistoryDTO> getMovementsByDateRange(LocalDateTime start, LocalDateTime end) {
        return movementHistoryRepository.findByMovementDateBetween(start, end)
			.stream()
			.map(accountMovementHistory -> this.toDto(accountMovementHistory))
			.toList();
    }

	private AccountMovementHistoryDTO toDto(AccountMovementHistory movement) {
		AccountMovementHistoryDTO dto = new AccountMovementHistoryDTO();
		dto.setAccountMovementHistoryId(movement.getAccountMovementHistoryId().toString());
		dto.setAccountId(movement.getAccount().getAccountId().toString());
		dto.setAmount(movement.getAmount());
		dto.setMovementType(movement.getMovementType());
		dto.setBalanceAfterMovement(movement.getBalanceAfterMovement());
		dto.setBalanceBeforeMovement(movement.getBalanceBeforeMovement());
		dto.setMovementDate(movement.getMovementDate());
		return dto;
	}
}
