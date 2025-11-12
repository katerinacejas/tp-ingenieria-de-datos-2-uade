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
	private Long invoiceId;

	@ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
	private User user; 

	@Column(nullable = false)
	private LocalDateTime issueDate = LocalDateTime.now();

	@ElementCollection
	@CollectionTable(name = "invoice_processes", joinColumns = @JoinColumn(name = "invoice_id"))
	@Column(name = "process_name")
	private List<ProcessRequest> billedProcesses = new ArrayList<ProcessRequest>();

	@Column(nullable = false)
	private String status; 
}
