package com.poliglota.vista;

import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class VistaAdministrador extends Vista{
	private String mailAutenticado;
	private final Scanner scanner;

	public VistaAdministrador (String mailAutenticado, Scanner scanner) {
		this.mailAutenticado = mailAutenticado;
		this.scanner = scanner;
	}
	
	@Override
	public void home() {
		
	}
}
