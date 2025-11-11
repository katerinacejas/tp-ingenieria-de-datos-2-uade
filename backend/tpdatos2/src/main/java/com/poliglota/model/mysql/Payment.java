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
    private Long id;

	@ManyToOne(optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

	@Column(nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

	@Column(nullable = false)
    private double amount;

	@Column(nullable = false)
    private String paymentMethod;
}
