package com.poliglota.vista;

import com.poliglota.controller.*;
import com.poliglota.model.mongo.Sensor;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import com.poliglota.DTO.AlertsDTO;
import com.poliglota.DTO.InvoiceDTO;
import com.poliglota.DTO.request.LoginRequestDTO;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import org.springframework.http.ResponseEntity;
import com.poliglota.DTO.ProcessDTO;
import com.poliglota.DTO.ProcessRequestDTO;

@Component
public class Vista {
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
	
	private final Scanner scanner;

	private String mailAutenticado;
	private Long idAutenticado;

	public Vista(
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
            UsuarioController usuarioController
    ) {
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

        this.scanner = new Scanner(System.in);
    }

	public void start() {
		// registro del usuario admin
		RegistroRequestDTO registro = new RegistroRequestDTO();
		registro.setEmail("admin@admin.com");
		registro.setNombreCompleto("admin");
		registro.setPassword("Test1234");
		registro.setRol("ADMIN");
		ResponseEntity<String> response = authenticationController.register(registro);
		if (response != null && "ok".equals(response.getBody())) {
			System.out.println("usuario administrador por default generado OK");
		}
		else {
			System.out.println("no se pudo generar el usuario admin defult");
		}

		String opcion;
		System.out.println(" 1. Registrarse");
		System.out.println(" 2. Iniciar Sesion");

		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				registrarse();
				break;
			case "2":
				iniciarSesion();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}
	private void registrarse() {
		System.out.println("Nombre completo: ");
		String nombreCompleto = scanner.nextLine();

		System.out.println("Email: ");
		String email = scanner.nextLine();

		System.out.println("Contraseña: ");
		String contrasenia = scanner.nextLine();

		RegistroRequestDTO registro = new RegistroRequestDTO();
		registro.setEmail(email);
		registro.setNombreCompleto(nombreCompleto);
		registro.setPassword(contrasenia);
		registro.setRol("USUARIO");

		ResponseEntity<String> response = authenticationController.register(registro);
		if (response != null && "ok".equals(response.getBody())) {
			System.out.println("Registro exitoso, puede iniciar sesion");
			start();
		}
		else {
			System.out.println("ocurrio un error, vuelva a registrarse");
			registrarse();
		}
	}

	private void iniciarSesion() {
		System.out.println("Email: ");
		String email = scanner.nextLine();

		System.out.println("Contraseña: ");
		String contrasenia = scanner.nextLine();

		LoginRequestDTO login = new LoginRequestDTO();
		login.setEmail(email);
		login.setPassword(contrasenia);

		ResponseEntity<String> response = authenticationController.login(login);
		System.out.println("respuesta del autenticador controller: " + response.getBody());
		if (response != null && "USUARIO".equals(response.getBody())) {
			System.out.println("Inicio de sesion exitoso, rol USUARIO");
			this.mailAutenticado = email;
			funcionalidadesUsuario();
		}
		if (response != null && "MANTENIMIENTO".equals(response.getBody())) {
			this.mailAutenticado = email;
			System.out.println("Inicio de sesion exitoso, rol MANTENIMIENTO");
			funcionalidadesMantenimiento();
		}
		if (response != null && "ADMIN".equals(response.getBody())) {
			this.mailAutenticado = email;
			System.out.println("Inicio de sesion exitoso, rol MANTENIMIENTO");
			funcionalidadesAdmin();
		}
		if (response != null && "Error".equals(response.getBody())) {
			System.out.println("Ocurrio un error (exception), inicie nuevamente");
			iniciarSesion();
		}
	}

	private void funcionalidadesUsuario() {
		String opcion;

		System.out.println(" 1. Modulo facturas");
		System.out.println(" 2. Modulo sensores");
		System.out.println(" 3. Modulo Alertas");
		System.out.println(" 4. Modulo procesos");
		System.out.println(" 5. Modulo chat");
		System.out.println(" 6. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				moduloFacturas();
				break;
			case "2":
				moduloSensores();
				break;
			case "3":
				moduloAlertas();
				break;
			case "4":
				moduloProcesos();
				break;
			case "5":
				moduloChat();
				break;
			case "6":
				cerrarSesion();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void funcionalidadesMantenimiento() {

	}

	private void funcionalidadesAdmin() {

	}

	private void moduloFacturas() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<InvoiceDTO>> facturasDTO = invoiceController.getInvoicesByUser(usuario.getUserId());
		
		List<InvoiceDTO> facturas = facturasDTO.getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No tenés facturas registradas.");
			funcionalidadesUsuario();
		}

		System.out.println("===== MIS FACTURAS =====");
		for (InvoiceDTO f : facturas) {
			System.out.println("----------------------------");
			System.out.println("ID factura: " + f.getInvoiceId());
			System.out.println("Fecha emisión: " + f.getIssueDate());
			System.out.println("Estado: " + f.getStatus());
			for (String billedProcess : f.getBilledProcessesIds()){
				System.out.println("Id de proceso facturado: " + billedProcess);
			}
		}
		System.out.println("----------------------------");
		System.out.println("Total de facturas: " + facturas.size());

		String opcion;

		System.out.println(" 1. Pagar una factura");
		System.out.println(" 2. Volver al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				System.out.println("Ingrese el ID de la factura que quiere pagar: ");
				Long idFactura;
				idFactura = scanner.nextLong();
				scanner.nextLine(); // limpia el salto de línea
				pagarUnaFactura(idFactura);
				break;
			case "2":
				funcionalidadesUsuario();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void pagarUnaFactura(Long idFactura) {
		invoiceController.updateStatus(idFactura, "pagada");
		System.out.println("Factura" + idFactura + "pagada correctamente");
		funcionalidadesUsuario();
	}

	private void moduloSensores() {
		String opcion;

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
				funcionalidadesUsuario();
				break;
			default:
				System.out.println("Opcion invalida.");
		}

	}

	private void verTodosLosSensores() {
		ResponseEntity<List<Sensor>> sensoresDTO = sensorController.list();
		
		List<Sensor> sensores = sensoresDTO.getBody();

		if (sensores == null || sensores.isEmpty()) {
			System.out.println("No hay sensores creados.");
			funcionalidadesUsuario();
		}

		System.out.println("===== SENSORES =====");
		for (Sensor s : sensores) {
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
		String nombre = scanner.nextLine();

		System.out.println("tipo (TEMPERATURA / HUMEDAD): ");
		String tipo = scanner.nextLine();

		if(!tipo.equals("TEMPERATURA") || !tipo.equals("HUMEDAD")){
			System.out.println("Tipo de sensor ingresado incorrecto, ingrese de nuevo (TEMPERATURA / HUMEDAD): ");
			tipo = scanner.nextLine();
		}

		System.out.println("ciudad: ");
		String ciudad = scanner.nextLine();

		System.out.println("pais: ");
		String pais = scanner.nextLine();

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

	private void moduloAlertas() {
		String opcion;

		System.out.println(" 1. Ver todas las alertas");
		System.out.println(" 2. Crear una alerta");
		System.out.println(" 3. Regresar al menu anterior");
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
				funcionalidadesUsuario();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void verTodasLasAlertas() {
		ResponseEntity<List<AlertsDTO>> alertasDTO = alertsController.getAllAlerts();
		
		List<AlertsDTO> alertas = alertasDTO.getBody();

		if (alertas == null || alertas.isEmpty()) {
			System.out.println("No hay alertas creadas.");
			funcionalidadesUsuario();
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
		String tipo = scanner.nextLine();
		
		if(!tipo.equals("SENSOR") || !tipo.equals("CLIMATICA")){
			System.out.println("Tipo de alerta ingresado incorrecto, ingrese de nuevo  (SENSOR / CLIMATICA): ");
			tipo = scanner.nextLine();
		}

		System.out.println("descripcion: ");
		String descripcion = scanner.nextLine();

		verTodosLosSensores();
		System.out.println("ID del sensor al que aplicarle la alerta: ");
		String idSensor = scanner.nextLine();

		AlertsDTO alerta = new AlertsDTO();
		alerta.setType(tipo);
		alerta.setState("ACTIVA");
		alerta.setDescripction(descripcion);
		alerta.setSensorId(idSensor);
		alerta.setDatetime(LocalDateTime.now());

		alertsController.createAlert(alerta);
		System.out.println("Alerta creada correctamente");
	}

	private void moduloChat() {

	}

	private void moduloProcesos() {
		String opcion;

		System.out.println(" 1. Ver todos los procesos");
		System.out.println(" 2. Solicitar crear un proceso");
		System.out.println(" 3. Regresar al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodosLosProcesos();
				moduloProcesos();
				break;
			case "2":
				solicitarCrearProceso();
				moduloProcesos();
				break;
			case "3":
				funcionalidadesUsuario();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void verTodosLosProcesos() {
		ResponseEntity<List<ProcessDTO>> procesosDTO = processController.getAll();
		List<ProcessDTO> procesos = procesosDTO.getBody();

		if (procesos == null || procesos.isEmpty()) {
			System.out.println("No hay procesos creados.");
			funcionalidadesUsuario();
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

	private void solicitarCrearProceso() {
		System.out.println("nombre");
		String nombre = scanner.nextLine();
		
		System.out.println("tipo de proceso: ");
		String tipo = scanner.nextLine();

		System.out.println("descripcion: ");
		String descripcion = scanner.nextLine();

		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);

		ProcessRequestDTO solicitud = new ProcessRequestDTO();
		solicitud.setUserId(usuario.getUserId().toString());
		solicitud.setProcessId(null);
		solicitud.setStatus("PENDIENTE");
		solicitud.setRequestDate(LocalDateTime.now());
		solicitud.setInvoiceId(null);
		solicitud.setName(nombre);
		solicitud.setDescripcion(descripcion);
		solicitud.setProcessType(tipo);

		processRequestController.createProcessRequest(solicitud);

		System.out.println("Solcitud de proceso creada correctamente");		
	}

	private void cerrarSesion() {

	}
}
