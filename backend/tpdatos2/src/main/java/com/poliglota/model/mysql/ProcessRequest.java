package com.poliglota.model.mysql;

import java.time.LocalDate;
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
    @JoinColumn(name = "process_id", nullable = false)
    private Process process;

	@Column(nullable = false)
    private LocalDateTime requestDate;

	@Column(nullable = false)
    private String status; 

	@OneToOne(optional = false)
	@JoinColumn(name = "invoice_id")
    private Invoice invoice;

	@Column(length = 100)
    private String city;      

    @Column(length = 100)
    private String country;  

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

	@Column(nullable = false)
	private String agrupacionDeDatos;

	public double getCostProcess() {
		return process.getCost();
	}
}