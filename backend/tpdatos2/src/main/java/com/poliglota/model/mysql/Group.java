package com.poliglota.model.sql;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Miembros del grupo (lista de IDs de usuarios)
    @ElementCollection
    @CollectionTable(name = "group_members", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "member_id")
    private List<Long> memberIds = new ArrayList<>();
}
