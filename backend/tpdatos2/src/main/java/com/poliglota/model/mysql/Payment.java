package com.poliglota.model.mysql;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
    private Long paymentId;

	@OneToOne(optional = false)
	@JoinColumn(name = "invoice_id")
    private Invoice invoice;

	@Column(nullable = false)
    private LocalDateTime paymentDate;

	@Column(nullable = false)
    private double amount;

	@Column(nullable = false)
    private String paymentMethod;
}
