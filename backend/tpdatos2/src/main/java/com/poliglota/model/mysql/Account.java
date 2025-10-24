package com.poliglota.model.sql;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // autoincrement en MySQL

    @Column(nullable = false)
    private Long userId; // referencia a usuario

    @Column(nullable = false)
    private double currentBalance;

    // Se puede crear una tabla hija o almacenarlo como JSON
    @ElementCollection
    @CollectionTable(name = "account_transactions", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "transaction_detail")
    private List<String> transactionHistory = new ArrayList<>();
}
