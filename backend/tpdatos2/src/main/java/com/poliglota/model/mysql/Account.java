package com.poliglota.model.mysql;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	@OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
	private User userId;

	@Column(nullable = false)
	private double currentBalance;

}
