package com.poliglota.DTO.response;

import lombok.Data;
import com.poliglota.model.mysql.Rol;

@Data
public class JwtResponseDTO {
    private String token;
	private Rol rol;

	public JwtResponseDTO(String token, Rol rol) {
        this.token = token;
        this.rol   = rol;
    }
}
