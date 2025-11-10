package com.poliglota.model.mysql;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "execution_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String executionId;

	@ManyToOne
    @JoinColumn(name = "request_id")
	private ProcessRequest processRequest;

	@Column(name = "execution_date", nullable = false)
	private LocalDateTime executionDate;

	@Column(name = "result", columnDefinition = "TEXT")
	private String result;

	@Column(name = "status", nullable = false)
	private String status;

}
