package com.poliglota.DTO.response;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long userId;
    private String nombreCompleto;
    private String email;
	private String rol;
}
