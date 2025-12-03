package com.poliglota.model.mysql;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id")
	private Long invoiceId;

	@ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
	private User user; 

	@Column(nullable = false)
	private LocalDateTime issueDate = LocalDateTime.now();

	@OneToOne(optional = false)
	@JoinColumn(name = "process_request_id")
	private ProcessRequest billedProcessRequest;

	@Column(nullable = false)
	private String status; 

	public double calculateTotalAmount() {
		return billedProcessRequest.getCostProcess();
	}
}
