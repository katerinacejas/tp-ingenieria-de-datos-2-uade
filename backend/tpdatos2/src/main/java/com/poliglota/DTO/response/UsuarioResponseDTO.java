package com.poliglota.DTO.response;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nombreCompleto;
    private String email;
	private String rol;
}
