package com.poliglota.model.mysql;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "processes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String processType;

    @Column(nullable = false)
    private double cost;
}
