package com.poliglota.model.mysql;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rol_entity_id")
	private Long rolEntityId;

	@Enumerated(EnumType.STRING)
	@Column(name = "code", unique = true, nullable = false, length = 32)
	private Rol code;

	@Column(name = "descripcion", nullable = false, length = 100)
	private String descripcion;

}
