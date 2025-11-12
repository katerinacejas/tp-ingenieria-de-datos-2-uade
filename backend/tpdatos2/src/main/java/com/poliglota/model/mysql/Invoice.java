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

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProcessRequest> billedProcesses = new ArrayList<ProcessRequest>();

	@Column(nullable = false)
	private String status; 
}
