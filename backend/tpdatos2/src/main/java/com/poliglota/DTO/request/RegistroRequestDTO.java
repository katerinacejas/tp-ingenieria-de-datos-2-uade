package com.poliglota.DTO.request;

import lombok.Data;

@Data
public class RegistroRequestDTO {
    private String nombreCompleto;
    private String email;
    private String password;
	private String rol;
}
