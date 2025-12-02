package com.poliglota.vista;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.poliglota.DTO.AlertsDTO;
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
import com.poliglota.model.mongo.Sensor;
import com.poliglota.service.ProcessService;

@Component
public class VistaMantenimiento extends Vista{
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

	public VistaMantenimiento (String mailAutenticado, Scanner scanner,
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
		VistaCompartida vistaGeneral
	) {
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
		System.out.println(" 1. Ver todos los sensores");
		System.out.println(" 2. Crear un sensor");
		System.out.println(" 3. Modificar datos / estado de un sensor");
		System.out.println(" 4. Realizar un control de funcionamiento de sensor");
		System.out.println(" 5. Ver historial de controles de funcionamiento de sensores");
		System.out.println(" 6. Modulo chat privado/grupal");
		System.out.println(" 7. Ver catalogo de procesos disponibles");
		System.out.println(" 8. Ver solicitudes de procesos de todos los usuarios");
		System.out.println(" 9. Aprobar (y ejecutar) / Rechazar solicitudes de procesos");
		System.out.println(" 10. Ver historial de ejecuciones de procesos");
		System.out.println(" 11. Ver todas las alertas");
		System.out.println(" 12. Resolver alertas activas");
		System.out.println(" 13. Ver facturas pendientes de pago de usuarios");
		System.out.println(" 14. Ver todas las facturas de usuarios");
		System.out.println(" 15. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				
				home();
				break;
			case "2":
				
				home();
				break;
			case "3":
				
				home();
				break;
			case "4":
				
				home();
				break;
			case "5":
				
				home();
				break;								
			case "6":
				vistaGeneral.moduloChat(mailAutenticado, this);
				home();
				break;
			case "7":
				verCatalogoProcesosDisponibles();
				home();
				break;
			case "8":
				
				home();
				break;
			case "9":
				
				home();
				break;
			case "10":
				
				home();
				break;
			case "11":
				
				home();
				break;
			case "12":
				
				home();
				break;
			case "13":
				
				home();
				break;
			case "14":
				
				home();
				break;																																
			case "15":
				cerrarSesion();
				vistaGeneral.home();
				break;
			default:
				System.out.println("Opcion invalida.");
				home();
		}
	}

	private void verCatalogoProcesosDisponibles() {
		vistaGeneral.verCatalogoProcesosDisponibles();
	}

	/*
			************************************************************
			******************** MODULO SENSORES ***********************
			************************************************************
	*/	

	private void moduloSensores() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println(" 1. Ver todos los sensores");
		System.out.println(" 2. Crear un sensor");
		System.out.println(" 3. Regresar al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodosLosSensores();
				moduloSensores();
				break;
			case "2":
				crearUnSensor();
				moduloSensores();
				break;
			case "3":
				home();
				break;
			default:
				System.out.println("Opcion invalida.");
				moduloSensores();
		}
	}

	private void verTodosLosSensores() {
		ResponseEntity<List<SensorDTO>> sensoresDTO = sensorController.list();
		
		List<SensorDTO> sensores = sensoresDTO.getBody();

		if (sensores == null || sensores.isEmpty()) {
			System.out.println("No hay sensores creados.");
			return;
		}

		System.out.println("===== SENSORES =====");
		for (SensorDTO s : sensores) {
			System.out.println("----------------------------");
			System.out.println("ID sensor: " + s.getId());
			System.out.println("Nombre: " + s.getName());
			System.out.println("tipo : " + s.getType());
			System.out.println("ciudad: " + s.getCity());
			System.out.println("pais: " + s.getCountry());
			System.out.println("Estado: " + s.getEstado());
			System.out.println("fecha de inicio: " + s.getStartDate());
		System.out.println("----------------------------");
		}
	}
	
	private void crearUnSensor() {
		System.out.println("nombre: ");
		String nombre = scanner.nextLine().trim();

		System.out.println("tipo (TEMPERATURA / HUMEDAD): ");
		String tipo = scanner.nextLine().trim();

		if(!tipo.equals("TEMPERATURA") || !tipo.equals("HUMEDAD")){
			System.out.println("Tipo de sensor ingresado incorrecto, ingrese de nuevo (TEMPERATURA / HUMEDAD): ");
			tipo = scanner.nextLine().trim();
		}

		System.out.println("ciudad: ");
		String ciudad = scanner.nextLine().trim();

		System.out.println("pais: ");
		String pais = scanner.nextLine().trim();

		Sensor sensor = new Sensor();
		sensor.setName(nombre);
		sensor.setType(tipo);
		sensor.setCity(ciudad);
		sensor.setCountry(pais);
		sensor.setEstado("INACTIVO");
		sensor.setStartDate(LocalDateTime.now());
		
		sensorController.create(sensor);
		System.out.println("Sensor " + nombre + "creado correctamente");
	}

	/*
			************************************************************
			********************* MODULO ALERTAS ***********************
			************************************************************
	*/	

	private void moduloAlertas() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println(" 1. Ver todas las alertas");
		System.out.println(" 2. Crear una alerta");
		System.out.println(" 3. Resolver una alerta");
		System.out.println(" 4. Regresar al menu anterior");

		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodasLasAlertas();
				moduloAlertas();
				break;
			case "2":
				crearUnaAlerta();
				moduloAlertas();
				break;
			case "3":
				resolverUnaAlerta();
				moduloAlertas();
				break;
			case "4":
				moduloAlertas();
				break;
			default:
				System.out.println("Opcion invalida.");
				moduloAlertas();
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
			System.out.println("estado: "+ a.getState());
			System.out.println("sensor id: " + a.getSensorId());
			System.out.println("fecha: " + a.getDatetime());
			System.out.println("descripcion: " + a.getDescripction());
		System.out.println("----------------------------");
		}		
	}

	private void crearUnaAlerta() {
		System.out.println("tipo de alerta (SENSOR / CLIMATICA): ");
		String tipo = scanner.nextLine().trim();
		
		if(!tipo.equals("SENSOR") || !tipo.equals("CLIMATICA")){
			System.out.println("Tipo de alerta ingresado incorrecto, ingrese de nuevo  (SENSOR / CLIMATICA): ");
			tipo = scanner.nextLine().trim();
		}

		System.out.println("descripcion: ");
		String descripcion = scanner.nextLine().trim();

		verTodosLosSensores();
		System.out.println("ID del sensor al que aplicarle la alerta: ");
		String idSensor = scanner.nextLine().trim();

		AlertsDTO alerta = new AlertsDTO();
		alerta.setType(tipo);
		alerta.setState("ACTIVA");
		alerta.setDescripction(descripcion);
		alerta.setSensorId(idSensor);
		alerta.setDatetime(LocalDateTime.now());

		alertsController.createAlert(alerta);
		System.out.println("Alerta creada correctamente");
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
	
	/*
			************************************************************
			******************** MODULO PROCESOS ***********************
			************************************************************
	*/	

	private void moduloProcesos() {
		String opcion;

		System.out.println(" 1. Ver todos los procesos");
		System.out.println(" 2. Aceptar una solicitud de proceso");
		System.out.println(" 4. Regresar al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodosLosProcesos();
				moduloProcesos();
				break;
			case "2":
				aceptarSolicitudProceso();
				moduloProcesos();
				break;
			case "3":
				home();
				break;
			default:
				System.out.println("Opcion invalida.");
				moduloProcesos();
		}
	}

	private void verTodosLosProcesos() {
		ResponseEntity<List<ProcessDTO>> procesosDTO = processController.getAll();
		List<ProcessDTO> procesos = procesosDTO.getBody();

		if (procesos == null || procesos.isEmpty()) {
			System.out.println("No hay procesos creados.");
			return;
		}

		System.out.println("===== PROCESOS =====");
		for (ProcessDTO p : procesos) {
			System.out.println("----------------------------");
			System.out.println("Nombre: " + p.getName());
			System.out.println("descripcion : " + p.getDescription());
			System.out.println("tipo: " +p.getProcessType());
			System.out.println("costo: " + p.getCost());
		System.out.println("----------------------------");
		}
	}

	private void aceptarSolicitudProceso() {
		ResponseEntity<List<ProcessRequestDTO>> listaSolicitudesTodas = processRequestController.getAll();
		List<ProcessRequestDTO> solicitudesTodas = listaSolicitudesTodas.getBody();
		if (solicitudesTodas == null || solicitudesTodas.isEmpty()) {
			System.out.println("No hay solciitudes de procesos creadas.");
			return;
		}
		List<ProcessRequestDTO> solicitudesPendientes = solicitudesTodas.stream()
			.filter(s -> "PENDIENTE".equalsIgnoreCase(s.getStatus()))
			.collect(Collectors.toList());

		if (solicitudesPendientes == null || solicitudesPendientes.isEmpty()) {
			System.out.println("No hay solciitudes de procesos PENDIENTES de aprobar.");
			return;
		}
		System.out.println("Seleccione que solicitud de proceso desea aprobar");
		for (ProcessRequestDTO soli : solicitudesPendientes){
			System.out.println(soli.getRequestId() +" - " + soli.getDescripcion());
		}
		String seleccionada = scanner.nextLine().trim();
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ProcessRequestRequestDTO soliDTO = new ProcessRequestRequestDTO();
		soliDTO.setUserId(usuario.getUserId().toString());
		soliDTO.setStatus("COMPLETADA");

		ProcessRequestDTO solicitudSeleccionada = null;
		for (ProcessRequestDTO s : solicitudesPendientes) {
			if (seleccionada.equals(s.getRequestId())) {
				solicitudSeleccionada = s;
				break;
			}
		}

		ProcessDTO proceso = new ProcessDTO();
		proceso.setName(solicitudSeleccionada.getName());
		proceso.setDescription(solicitudSeleccionada.getDescripcion());
		proceso.setProcessType(solicitudSeleccionada.getProcessType());
		proceso.setCost(300);
		ResponseEntity<ProcessDTO> procesoCreado = processController.create(proceso);
		soliDTO.setProcessId(procesoCreado.getBody().getId().toString());

		processRequestController.updateStatusProcessRequest(soliDTO);
		System.out.println("Solicitud de proceso aprobada correctamente");
	}

	private void cerrarSesion() {
		String mail = this.mailAutenticado;
		this.mailAutenticado = null;
		vistaGeneral.cerrarSesion(mail);
	}
}
