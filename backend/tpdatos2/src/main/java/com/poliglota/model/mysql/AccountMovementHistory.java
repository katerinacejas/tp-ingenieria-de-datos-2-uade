package com.poliglota.model.mysql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_movements_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountMovementHistory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountMovementHistoryId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private double amount;

	@Column(nullable = false)
	private String movementType;

	@Column(nullable = false)
	private double balanceAfterMovement;

	@Column(nullable = false)
	private double balanceBeforeMovement;

    @Column(nullable = false)
    private LocalDateTime movementDate;
}
