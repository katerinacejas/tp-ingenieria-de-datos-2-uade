package com.poliglota.DTO.request;

import lombok.Data;
import java.sql.Date;

@Data
public class RegistroRequestDTO {
    private String nombreCompleto;
    private String direccion;
    private int telefono;
    private Date fechaNacimiento;
    private String email;
    private String password;
	private String rol;
}
