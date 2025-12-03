package com.poliglota.vista;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.poliglota.DTO.AlertsDTO;
import com.poliglota.DTO.ExecutionHistoryDTO;
import com.poliglota.DTO.ProcessDTO;
import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.DTO.SensorDTO;
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
import com.poliglota.controller.ExecutionHistoryController;
import com.poliglota.model.mongo.Sensor;
import com.poliglota.service.ProcessService;

public class VistaMantenimiento extends Vista {
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
	private final ExecutionHistoryController executionHistoryController;

	public VistaMantenimiento(String mailAutenticado, Scanner scanner,
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
			VistaCompartida vistaGeneral,
			ExecutionHistoryController executionHistoryController) {
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
		this.executionHistoryController = executionHistoryController;
	}

	@Override
	public void home() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println(" 1. Ver todos los sensores");
		System.out.println(" 2. Crear un sensor");
		System.out.println(" 3. Modificar estado de un sensor");
		System.out.println(" 4. Modulo chat privado/grupal");
		System.out.println(" 5. Ver catalogo de procesos disponibles");
		System.out.println(" 6. Ver solicitudes de procesos de todos los usuarios");
		System.out.println(" 7. Aprobar (y ejecutar) / Rechazar solicitudes de procesos");
		System.out.println(" 8. Ver historial de ejecuciones de procesos");
		System.out.println(" 9. Ver todas las alertas");
		System.out.println(" 10. Resolver alertas activas");
		System.out.println(" 11. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodosLosSensores();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "2":
				crearUnSensor();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "3":
				modificarEstadoSensor();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "4":
				vistaGeneral.moduloChat(mailAutenticado, this);
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "5":
				verCatalogoProcesosDisponibles();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "6":
				verTodasLasSolicitudes();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "7":
				aprobarEjecutarSolicitud();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "8":
				verHistorialEjecucionesProcesos();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "9":
				verTodasLasAlertas();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "10":
				resolverUnaAlerta();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "11":
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

	private void crearUnSensor() {
		System.out.println("Nombre: ");
		String nombre = scanner.nextLine().trim();

		System.out.println("Tipo (TEMPERATURA / HUMEDAD): ");
		String tipo = scanner.nextLine().trim();

		if (!tipo.equals("TEMPERATURA") || !tipo.equals("HUMEDAD")) {
			System.out.println("Tipo de sensor ingresado incorrecto, ingrese de nuevo (TEMPERATURA / HUMEDAD): ");
			tipo = scanner.nextLine().trim();
		}

		System.out.println("Ciudad: ");
		String ciudad = scanner.nextLine().trim();

		System.out.println("Pais: ");
		String pais = scanner.nextLine().trim();

		SensorDTO sensor = new SensorDTO();
		sensor.setName(nombre);
		sensor.setType(tipo);
		sensor.setCity(ciudad);
		sensor.setCountry(pais);
		sensor.setEstado("ACTIVO");
		sensor.setStartDate(LocalDateTime.now());

		sensorController.create(sensor);
		System.out.println("Sensor " + nombre + "creado correctamente");
	}

	private void modificarEstadoSensor() {
		vistaGeneral.verTodosLosSensores();
		System.out.println("Seleccione el ID del sensor al cual modificarle el estado");
		String idSensor = scanner.nextLine().trim();
		System.out.println("Ingrese el estado nuevo del sensor");
		String estado = scanner.nextLine().trim();
		sensorController.updateStateSensor(idSensor, estado);
		System.out.println("Sensor modificado correctamente");
	}

	private void verTodasLasSolicitudes() {
		List<ProcessRequestDTO> lista = processRequestController.getAll().getBody();

		if (lista == null || lista.isEmpty()) {
			System.out.println("No hay solicitudes de procesos creadas.");
			return;
		}

		ProcessDTO p;
		UsuarioResponseDTO usuario;
		System.out.println("===== SOLICITUDES DE PROCESOS DE USUARIOS =====");
		for (ProcessRequestDTO s : lista) {
			p = processController.getById(Long.parseLong(s.getProcessId())).getBody();
			usuario = usuarioController.getUsuarioPorId(Long.parseLong(s.getUserId()));
			System.out.println("----------------------------");
			System.out.println("ID de solicitud de proceso: " + s.getProcessRequestId());
			System.out.println("ID Usuario solicitante: " + s.getUserId());
			System.out.println("Nombre del usuario solicitante: " + usuario.getNombreCompleto());
			System.out.println("Nombre proceso: " + p.getName());
			System.out.println("Descripcion proceso: " + p.getDescription());
			System.out.println("Tipo de proceso: " + p.getProcessType());
			System.out.println("Estado solicitud: " + s.getStatus());
			System.out.println("Fecha solicitud: " + s.getRequestDate());
			System.out.println("Parametro ciudad: " + s.getCity());
			System.out.println("Parametro pais: " + s.getCountry());
			System.out.println("Parametro fecha inicio: " + s.getStartDate());
			System.out.println("Parametro fecha fin: " + s.getEndDate());
			System.out.println("Parametro agrupacion de datos: " + s.getAgrupacionDeDatos());
		}
	}

	private void aprobarEjecutarSolicitud() {
		List<ProcessRequestDTO> lista = processRequestController.getProcessRequestByStatus("PENDIENTE").getBody();

		if (lista == null || lista.isEmpty()) {
			System.out.println("No hay solicitudes de procesos pendientes de aprobar.");
			return;
		}

		ProcessDTO p;

		for (ProcessRequestDTO soli : lista) {
			p = processController.getById(Long.parseLong(soli.getProcessId())).getBody();
			System.out.println("----------------------------");
			System.out.println("ID de solicitud de proceso: " + soli.getProcessRequestId());
			System.out.println("ID Usuario solicitante: " + soli.getUserId());
			System.out.println("Nombre proceso: " + p.getName());
			System.out.println("Descripcion proceso: " + p.getDescription());
			System.out.println("Tipo de proceso: " + p.getProcessType());
			System.out.println("Estado solicitud: " + soli.getStatus());
			System.out.println("Fecha solicitud: " + soli.getRequestDate());
			System.out.println("Parametro ciudad: " + soli.getCity());
			System.out.println("Parametro pais: " + soli.getCountry());
			System.out.println("Parametro fecha inicio: " + soli.getStartDate());
			System.out.println("Parametro fecha fin: " + soli.getEndDate());
			System.out.println("Parametro agrupacion de datos: " + soli.getAgrupacionDeDatos());
		}
		System.out.println("Seleccione que solicitud de proceso desea aprobar");

		String seleccionada = scanner.nextLine().trim();

		ProcessRequestDTO soliDTO = lista.stream().filter(soli -> soli.getProcessRequestId().equals(seleccionada))
				.findFirst().orElse(null);

		ExecutionHistoryDTO ejecucion = processRequestController
				.approveAndExecute(Long.parseLong(soliDTO.getProcessRequestId())).getBody();

		if (ejecucion == null) {
			System.out.println("Ocurrió un error al ejecutar el proceso.");
			return;
		}

		System.out.println("Solicitud de proceso aprobada y ejecutada correctamente.");
		System.out.println("===== RESULTADO DE LA EJECUCIÓN =====");
		System.out.println(ejecucion.getResult());

	}

	private void cerrarSesion() {
		String mail = this.mailAutenticado;
		this.mailAutenticado = null;
		vistaGeneral.cerrarSesion(mail);
	}
	
	private void verHistorialEjecucionesProcesos() {
		List<ExecutionHistoryDTO> lista = executionHistoryController.getAll().getBody();
		
		if (lista == null || lista.isEmpty()) {
			System.out.println("No hay ejecuciones de solicitudes de procesos.");
			return;
		}

		for (ExecutionHistoryDTO eh: lista) {
			System.out.println("===== RESULTADO DE LA EJECUCIÓN " + eh.getExecutionId() +" =====");
			System.out.println("----------------------------");
			System.out.println("Fecha de ejecucion: " + eh.getExecutionDate());
			System.out.println("Estado de la ejecucion: " + eh.getStatus());
			System.out.println(eh.getResult());
		}
	}

	private void verTodasLasAlertas() {
		ResponseEntity<List<AlertsDTO>> alertasDTO = alertsController.getAllAlerts();

		List<AlertsDTO> alertas = alertasDTO.getBody();

		if (alertas == null || alertas.isEmpty()) {
			System.out.println("No hay alertas creadas.");
			return;
		}

		System.out.println("===== ALERTAS =====");
		for (AlertsDTO a : alertas) {
			System.out.println("----------------------------");
			System.out.println("ID alerta: " + a.getAlertId());
			System.out.println("tipo : " + a.getType());
			System.out.println("estado: " + a.getState());
			System.out.println("sensor id: " + a.getSensorId());
			System.out.println("fecha: " + a.getDatetime());
			System.out.println("descripcion: " + a.getDescripction());
		}
	}

	private void resolverUnaAlerta() {
		ResponseEntity<List<AlertsDTO>> alertasDTO = alertsController.getAlertsByState("ACTIVA");

		List<AlertsDTO> alertas = alertasDTO.getBody();

		if (alertas == null || alertas.isEmpty()) {
			System.out.println("No hay alertas activas sin resolver.");
			return;
		}

		System.out.println("Seleccione la alerta que quiere resolver: ");
		for (AlertsDTO a : alertas) {
			System.out.println(a.getAlertId() + " - " + a.getDescripction());
		}

		String alertaAResolver;
		alertaAResolver = scanner.nextLine().trim();

		alertsController.updateAlertState(alertaAResolver, "RESUELTA");
		System.out.println("Se resolvio correctamente la alerta");
	}

}
