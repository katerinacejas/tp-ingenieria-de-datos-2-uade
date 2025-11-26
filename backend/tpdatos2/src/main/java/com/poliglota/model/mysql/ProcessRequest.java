package com.poliglota.model.mysql;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "process_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "process_request_id")
	private Long processRequestId;

	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

	@ManyToOne(optional = false)
    @JoinColumn(name = "process_id", nullable = true)
    private Process process;

	@Column(nullable = false)
    private String name; 
	@Column(nullable = false)
    private String descripcion; 

	@Column(nullable = false)
    private LocalDateTime requestDate;

	@Column(nullable = false)
    private String status; 

	@ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

	public double getCostProcess() {
		return process.getCost();
	}
}