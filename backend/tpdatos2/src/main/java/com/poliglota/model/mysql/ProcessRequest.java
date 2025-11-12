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
	private String processRequestId;

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

}