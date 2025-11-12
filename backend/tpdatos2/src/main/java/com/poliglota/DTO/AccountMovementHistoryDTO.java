package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AccountMovementHistoryDTO {
	private String accountMovementHistoryId;
	private String accountId;
	private double amount;
	private String movementType;
	private double balanceAfterMovement;
	private double balanceBeforeMovement;
	private LocalDateTime movementDate;
}
