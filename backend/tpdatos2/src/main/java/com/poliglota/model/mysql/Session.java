package com.poliglota.model.mysql;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private RolEntity rol;

	@Column(nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();

    private LocalDateTime endTime;

	@Column(nullable = false)
    private String status;
}
