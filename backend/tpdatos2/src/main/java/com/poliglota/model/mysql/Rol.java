package com.poliglota.model;


@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor

public enum Rol {
    USUARIO,
    ADMIN
}