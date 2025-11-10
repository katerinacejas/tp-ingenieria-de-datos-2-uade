package com.poliglota.model.mysql;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.poliglota.model.mysql.User;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;
    private String status = "ACTIVE"; // ACTIVE / INACTIVE
}
