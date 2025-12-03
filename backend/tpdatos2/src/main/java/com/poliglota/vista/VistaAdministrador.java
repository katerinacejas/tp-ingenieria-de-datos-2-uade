package com.poliglota.vista;

import java.util.List;
import java.util.Scanner;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.poliglota.controller.AccountController;
import com.poliglota.controller.AccountMovementHistoryController;
import com.poliglota.controller.AlertsController;
import com.poliglota.controller.AuthenticationController;
import com.poliglota.controller.GroupController;
import com.poliglota.controller.InvoiceController;
import com.poliglota.controller.MaintenanceCheckController;
import com.poliglota.controller.MeasurementController;
import com.poliglota.controller.MessageController;
import com.poliglota.controller.PaymentController;
import com.poliglota.controller.ProcessController;
import com.poliglota.controller.ProcessRequestController;
import com.poliglota.controller.SensorController;
import com.poliglota.controller.UsuarioController;
import com.poliglota.service.ProcessService;
import com.poliglota.DTO.*;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;

public class VistaAdministrador extends Vista {
	private String mailAutenticado;
	private final Scanner scanner;
	private final AccountController accountController;
	private final AccountMovementHistoryController accountMovementHistoryController;
	private final AlertsController alertsController;
	private final AuthenticationController authenticationController;
	private final GroupController groupController;
	private final InvoiceController invoiceController;
	private final MaintenanceCheckController maintenanceCheckController;
	private final MeasurementController measurementController;
	private final MessageController messageController;
	private final PaymentController paymentController;
	private final ProcessController processController;
	private final ProcessRequestController processRequestController;
	private final SensorController sensorController;
	private final UsuarioController usuarioController;
	private final ProcessService processService;
	private final VistaCompartida vistaGeneral;

	public VistaAdministrador(String mailAutenticado, Scanner scanner,
			AccountController accountController,
			AccountMovementHistoryController accountMovementHistoryController,
			AlertsController alertsController,
			AuthenticationController authenticationController,
			GroupController groupController,
			InvoiceController invoiceController,
			MaintenanceCheckController maintenanceCheckController,
			MeasurementController measurementController,
			MessageController messageController,
			PaymentController paymentController,
			ProcessController processController,
			ProcessRequestController processRequestController,
			SensorController sensorController,
			UsuarioController usuarioController,
			ProcessService processService,
			VistaCompartida vistaGeneral) {
		this.mailAutenticado = mailAutenticado;
		this.scanner = scanner;
		this.accountController = accountController;
		this.accountMovementHistoryController = accountMovementHistoryController;
		this.alertsController = alertsController;
		this.authenticationController = authenticationController;
		this.groupController = groupController;
		this.invoiceController = invoiceController;
		this.maintenanceCheckController = maintenanceCheckController;
		this.measurementController = measurementController;
		this.messageController = messageController;
		this.paymentController = paymentController;
		this.processController = processController;
		this.processRequestController = processRequestController;
		this.sensorController = sensorController;
		this.usuarioController = usuarioController;
		this.processService = processService;
		this.vistaGeneral = vistaGeneral;
	}

	@Override
	public void home() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println(" 1. Ver todos los usuarios");
		System.out.println(" 2. Registrar personal de mantenimiento");
		System.out.println(" 3. Registrar otro administrador");
		System.out.println(" 4. Ver todos los sensores");
		System.out.println(" 5. Ver catalogo de procesos disponibles");
		System.out.println(" 6. Crear nuevo proceso");
		System.out.println(" 7. Ver facturas pendientes de pago de usuarios");
		System.out.println(" 8. Ver todas las facturas de usuarios");
		System.out.println(" 9. Ver total facturado y total deuda");
		System.out.println(" 10. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodosLosUsuarios();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "2":
				registrarMantenimiento();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "3":
				registrarAdministrador();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "4":
				verTodosLosSensores();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "5":
				verCatalogoProcesosDisponibles();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "6":
				crearProceso();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "7":
				verFacturasPendientesDePago();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "8":
				verTodasLasFacturas();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "9":
				verTotalFacturadoDeuda();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "10":
				cerrarSesion();
				    System.out.println("=============================================\n\n");
				vistaGeneral.home();
				break;
			default:
				System.out.println("Opcion invalida.");
				    System.out.println("=============================================\n\n");
				home();
		}
	}

	private void verCatalogoProcesosDisponibles() {
		vistaGeneral.verCatalogoProcesosDisponibles();
	}

	private void verTodosLosSensores() {
		vistaGeneral.verTodosLosSensores();
	}

	private void cerrarSesion() {
		String mail = this.mailAutenticado;
		this.mailAutenticado = null;
		vistaGeneral.cerrarSesion(mail);
	}

	private void verTodosLosUsuarios() {
		List<UsuarioResponseDTO> lista = usuarioController.getTodosLosUsuarios();
		System.out.println("===== TODOS LOS USUARIOS =====");
		for (UsuarioResponseDTO u : lista) {
			System.out.println("----------------------------");
			System.out.println("ID del usuario: " + u.getUserId());
			System.out.println("Nombre completo: " + u.getNombreCompleto());
			System.out.println("Email: " + u.getEmail());
			System.out.println("Rol: " + u.getRol());
		}
	}

	private void registrarMantenimiento() {
		System.out.println("Nombre completo: ");
		String nombreCompleto = scanner.nextLine().trim();

		System.out.println("Email: ");
		String email = scanner.nextLine().trim();

		System.out.println("Contraseña: ");
		String contrasenia = scanner.nextLine().trim();

		RegistroRequestDTO registro = new RegistroRequestDTO();
		registro.setEmail(email);
		registro.setNombreCompleto(nombreCompleto);
		registro.setPassword(contrasenia);
		registro.setRol("MANTENIMIENTO");

		ResponseEntity<String> response = authenticationController.registerMantenimiento(registro);
		if (response != null && "ok".equals(response.getBody())) {
			System.out.println("Registro exitoso del personal de mantenimiento");
		} else {
			System.out.println("ocurrio un error, vuelva a intentarlo");
		}
	}

	private void registrarAdministrador() {
		System.out.println("Nombre completo: ");
		String nombreCompleto = scanner.nextLine().trim();

		System.out.println("Email: ");
		String email = scanner.nextLine().trim();

		System.out.println("Contraseña: ");
		String contrasenia = scanner.nextLine().trim();

		RegistroRequestDTO registro = new RegistroRequestDTO();
		registro.setEmail(email);
		registro.setNombreCompleto(nombreCompleto);
		registro.setPassword(contrasenia);
		registro.setRol("ADMIN");

		ResponseEntity<String> response = authenticationController.registerAdmin(registro);
		if (response != null && "ok".equals(response.getBody())) {
			System.out.println("Registro exitoso del administrador");
		} else {
			System.out.println("ocurrio un error, vuelva a intentarlo");
		}
	}

	private void crearProceso() {
		System.out.println("Ingrese el nombre interno del proceso (ej: INFORME_HUMEDAD_MAX_ANUAL):");
		String nombre = scanner.nextLine().trim();

		System.out.println("Ingrese la descripción del proceso:");
		String descripcion = scanner.nextLine().trim();

		String processType = null;
		while (processType == null) {
			System.out.println("Seleccione el tipo de proceso:");
			System.out.println("1) MAXIMO");
			System.out.println("2) MINIMO");
			System.out.println("3) PROMEDIO");
			String opcionTipo = scanner.nextLine().trim();

			switch (opcionTipo) {
				case "1" -> processType = "MAXIMO";
				case "2" -> processType = "MINIMO";
				case "3" -> processType = "PROMEDIO";
				default -> System.out.println("Opción inválida, intente de nuevo.");
			}
		}

		Double cost = null;
		while (cost == null) {
			System.out.println("Ingrese el costo del proceso (en número, ej: 100.0):");
			String inputCost = scanner.nextLine().trim();
			try {
				cost = Double.parseDouble(inputCost);
			} catch (NumberFormatException e) {
				System.out.println("Valor inválido, ingrese un número.");
			}
		}

		ProcessDTO dto = new ProcessDTO();
		dto.setName(nombre);
		dto.setDescription(descripcion);
		dto.setProcessType(processType);
		dto.setCost(cost);

		try {
			ProcessDTO creado = processController.create(dto).getBody();
			System.out.println("Proceso creado correctamente.");
			System.out.println("ID: " + creado.getId());
			System.out.println("Nombre: " + creado.getName());
			System.out.println("Descripción: " + creado.getDescription());
			System.out.println("Tipo: " + creado.getProcessType());
			System.out.println("Costo: " + creado.getCost());
		} catch (Exception e) {
			System.out.println("Error al crear el proceso: " + e.getMessage());
		}
	}

	private void verFacturasPendientesDePago() {
		List<InvoiceDTO> facturas = invoiceController.getInvoicesByStatus("PENDIENTE").getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No hay facturas pendientes de pago.");
			return;
		}

		System.out.println("===== FACTURAS PENDIENTES DE PAGO =====");
		ProcessRequestDTO prdto;
		ProcessDTO pdto;
		UsuarioResponseDTO usuario;
		for (InvoiceDTO f : facturas) {
			prdto = processRequestController.getProcessRequestById(Long.parseLong(f.getBilledProcessRequest()))
					.getBody();
			pdto = processController.getById(Long.parseLong(prdto.getProcessId())).getBody();
			usuario = usuarioController.getUsuarioPorId(Long.parseLong(prdto.getUserId()));
			System.out.println("----------------------------");
			System.out.println("Usuario: " + usuario.getNombreCompleto());
			System.out.println("ID Usuario: " + usuario.getUserId());
			System.out.println("Fecha: " + f.getIssueDate());
			System.out.println("Estado de la factura: " + f.getStatus());
			System.out.println("Estado de la solicitud de proceso: " + prdto.getStatus());
			System.out.println("Nombre del proceso solicitado: " + pdto.getName());
			System.out.println("Descripcion del proceso solicitado: " + pdto.getDescription());
			System.out.println("Importe a pagar: " + pdto.getCost());
		}
	}

	private void verTodasLasFacturas() {
		List<InvoiceDTO> facturas = invoiceController.getAllInvoices().getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No hay facturas.");
			return;
		}

		System.out.println("===== FACTURAS =====");
		ProcessRequestDTO prdto;
		ProcessDTO pdto;
		UsuarioResponseDTO usuario;
		for (InvoiceDTO f : facturas) {
			prdto = processRequestController.getProcessRequestById(Long.parseLong(f.getBilledProcessRequest()))
					.getBody();
			pdto = processController.getById(Long.parseLong(prdto.getProcessId())).getBody();
			usuario = usuarioController.getUsuarioPorId(Long.parseLong(prdto.getUserId()));
			System.out.println("----------------------------");
			System.out.println("Usuario: " + usuario.getNombreCompleto());
			System.out.println("ID Usuario: " + usuario.getUserId());
			System.out.println("Fecha: " + f.getIssueDate());
			System.out.println("Estado de la factura: " + f.getStatus());
			System.out.println("Estado de la solicitud de proceso: " + prdto.getStatus());
			System.out.println("Nombre del proceso solicitado: " + pdto.getName());
			System.out.println("Descripcion del proceso solicitado: " + pdto.getDescription());
			System.out.println("Importe a pagar: " + pdto.getCost());
		}
	}

	private void verTotalFacturadoDeuda() {
		List<InvoiceDTO> facturas = invoiceController.getAllInvoices().getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No hay facturas.");
			return;
		}

		double totalFacturado = 0.0; 
		double totalDeuda = 0.0;
		ProcessRequestDTO prdto;
		ProcessDTO pdto;

		for (InvoiceDTO f : facturas) {
			prdto = processRequestController.getProcessRequestById(Long.parseLong(f.getBilledProcessRequest())).getBody();
			pdto = processController.getById(Long.parseLong(prdto.getProcessId())).getBody();
			double importe = pdto.getCost();

			if ("PAGADA".equalsIgnoreCase(f.getStatus())) {
				totalFacturado += importe;
			} else if ("PENDIENTE".equalsIgnoreCase(f.getStatus())) {
				totalDeuda += importe;
			}
		}

		System.out.println("===== RESUMEN FACTURACIÓN =====");
		System.out.println("Total facturado (facturas PAGADAS): " + String.format("%.2f", totalFacturado));
		System.out.println("Total adeudado (facturas PENDIENTES): " + String.format("%.2f", totalDeuda));
	}

}
