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
	private Long id;

	@Column(nullable = false)
	private Long userId; // referencia al usuario dueño

	private LocalDateTime issueDate = LocalDateTime.now();

	// Procesos facturados
	@ElementCollection
	@CollectionTable(name = "invoice_processes", joinColumns = @JoinColumn(name = "invoice_id"))
	@Column(name = "process_name")
	private List<String> billedProcesses = new ArrayList<>();

	@Column(nullable = false)
	private String status; // pending / paid / overdue
}
