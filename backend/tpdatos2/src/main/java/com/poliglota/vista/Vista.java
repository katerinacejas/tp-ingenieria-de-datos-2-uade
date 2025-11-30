package com.poliglota.vista;

import com.poliglota.controller.*;
import com.poliglota.model.mongo.Sensor;
import com.poliglota.model.mysql.ExecutionHistory;
import com.poliglota.service.UsuarioService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.kenai.jffi.Array;
import com.poliglota.DTO.AccountDTO;
import com.poliglota.DTO.AlertsDTO;
import com.poliglota.DTO.GroupDTO;
import com.poliglota.DTO.InvoiceDTO;
import com.poliglota.DTO.MessageDTO;
import com.poliglota.DTO.request.LoginRequestDTO;
import com.poliglota.DTO.request.ProcessRequestRequestDTO;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.request.SendDirectRequestDTO;
import com.poliglota.DTO.request.SendGroupRequestDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import com.poliglota.DTO.ProcessDTO;
import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.service.ProcessService;
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
	private final ProcessService processService;

	private final MongoTemplate mongoTemplate;
	
	private final Scanner scanner;

	private String mailAutenticado;

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
            UsuarioController usuarioController,
			ProcessService processService,
			MongoTemplate mongoTemplate,
    		UsuarioService usuarioService) {
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
		this.mongoTemplate = mongoTemplate;

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

		home();
	}

	private void home() {
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
			home();
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
		System.out.println(" 6. Ver mi cuenta corriente");
		System.out.println(" 7. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				moduloFacturas();
				funcionalidadesUsuario();
				break;
			case "2":
				moduloSensores();
				funcionalidadesUsuario();
				break;
			case "3":
				moduloAlertasUsuario();
				funcionalidadesUsuario();
				break;
			case "4":
				moduloProcesosUsuario();
				funcionalidadesUsuario();
				break;
			case "5":
				moduloChatUsuario();
				funcionalidadesUsuario();
				break;
			case "6":
				moduloCuentacorriente();
				funcionalidadesUsuario();
				break;				
			case "7":
				cerrarSesion();
				home();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void funcionalidadesMantenimiento() {
		String opcion;

		System.out.println(" 1. Modulo sensores");
		System.out.println(" 2. Modulo Alertas");
		System.out.println(" 3. Modulo procesos");
		System.out.println(" 4. Modulo chat");
		System.out.println(" 6. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				moduloSensores();
				funcionalidadesMantenimiento();
				break;
			case "2":
				moduloAlertasMantenimiento();
				funcionalidadesMantenimiento();
				break;
			case "3":
				moduloProcesosMantenimiento();
				funcionalidadesMantenimiento();
				break;
			case "4":
				moduloChatMantenimiento();
				funcionalidadesMantenimiento();
				break;
			case "5":
				ejecutarProceso();
				funcionalidadesMantenimiento();
				break;
			case "6":
				cerrarSesion();
				home();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void funcionalidadesAdmin() {

	}

	private void moduloCuentacorriente() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		AccountDTO cuenta = accountController.getAccountByUser(usuario.getUserId()).getBody();
		System.out.println("===== MI CUENTA CORRIENTE =====");
		System.out.println(cuenta.getCurrentBalance());
		String opcion;
		System.out.println("1. Depositar plata ");
		System.out.println("2. Regresar al menu anterior ");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				depositarPlata();
				break;
			case "2":
				funcionalidadesUsuario();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
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
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		AccountDTO cuenta = accountController.getAccountByUser(usuario.getUserId()).getBody();

		InvoiceDTO factura = invoiceController.getInvoiceById(idFactura).getBody();

		List<ProcessDTO> procesosDTO = new ArrayList<>();
		for(String procesoID : factura.getBilledProcessesIds()){
			procesosDTO.add(processController.getById(Long.parseLong(procesoID)).getBody());
		}
		
		double totalCost = procesosDTO.stream()
										.mapToDouble(ProcessDTO::getCost)
										.sum();

		accountController.withdraw(Long.parseLong(cuenta.getAccountId()), totalCost);

		System.out.println("Factura" + idFactura + "pagada correctamente");
		funcionalidadesUsuario();
	}

	private void depositarPlata() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		AccountDTO cuenta = accountController.getAccountByUser(usuario.getUserId()).getBody();
		System.out.println("ingrese la cantidad de dinero que quiere depositar: ");
		String plata = scanner.nextLine().trim();
		plata = plata.replace(",", ".");
    	double monto = Double.parseDouble(plata);
		accountController.deposit(Long.parseLong(cuenta.getAccountId()),monto);
		System.out.println("Dinero depositado correctamente");
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

	private void moduloAlertasUsuario() {
		String opcion;

		System.out.println(" 1. Ver todas las alertas");
		System.out.println(" 2. Crear una alerta");
		System.out.println(" 3. Regresar al menu anterior");

		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodasLasAlertas();
				moduloAlertasUsuario();
				break;
			case "2":
				crearUnaAlerta();
				moduloAlertasUsuario();
				break;
			case "3":
				funcionalidadesUsuario();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void moduloAlertasMantenimiento() {
		String opcion;

		System.out.println(" 1. Ver todas las alertas");
		System.out.println(" 2. Crear una alerta");
		System.out.println(" 3. Resolver una alerta");
		System.out.println(" 4. Regresar al menu anterior");

		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodasLasAlertas();
				moduloAlertasMantenimiento();
				break;
			case "2":
				crearUnaAlerta();
				moduloAlertasMantenimiento();
				break;
			case "3":
				resolverUnaAlerta();
				moduloAlertasMantenimiento();
				break;
			case "4":
				funcionalidadesMantenimiento();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void resolverUnaAlerta() {
		ResponseEntity<List<AlertsDTO>> alertasDTO = alertsController.getAlertsByState("ACTIVA");
		
		List<AlertsDTO> alertas = alertasDTO.getBody();

		if (alertas == null || alertas.isEmpty()) {
			System.out.println("No hay alertas activas sin resolver.");
			funcionalidadesUsuario();
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

	private void moduloChatUsuario() {
		String opcion;

		System.out.println(" 1. Ver los mensajes de una conversación directa");
		System.out.println(" 2. Ver los mensajes de un grupo");
		System.out.println(" 3. Enviar mensaje directo");
		System.out.println(" 4. Enviar mensaje a un grupo");
		System.out.println(" 5. Crear grupo");
		System.out.println(" 6. Regresar al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verMensajesDirectosUsuario();
				moduloChatUsuario();
				break;
			case "2":
				verMensajesGrupoUsuario();
				moduloChatUsuario();
				break;
			case "3":
				enviarMensajeDirectoUsuario();
				moduloChatUsuario();
				break;
			case "4":
				enviarMensajeGrupoUsuario();
				moduloChatUsuario();
				break;
			case "5":
				crearGrupoUsuario();
				moduloChatUsuario();
				break;
			case "6":
				funcionalidadesUsuario();
				break;				
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void moduloChatMantenimiento() {
		String opcion;

		System.out.println(" 1. Ver los mensajes de una conversación directa");
		System.out.println(" 2. Ver los mensajes de un grupo");
		System.out.println(" 3. Enviar mensaje directo");
		System.out.println(" 4. Enviar mensaje a un grupo");
		System.out.println(" 5. Crear grupo");
		System.out.println(" 6. Regresar al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verMensajesDirectosMantenimiento();
				moduloChatMantenimiento();
				break;
			case "2":
				verMensajesGrupoMantenimiento();
				moduloChatMantenimiento();
				break;
			case "3":
				enviarMensajeDirectoMantenimiento();
				moduloChatMantenimiento();
				break;
			case "4":
				enviarMensajeGrupoMantenimiento();
				moduloChatMantenimiento();
				break;
			case "5":
				crearGrupoMantenimiento();
				moduloChatMantenimiento();
				break;
			case "6":
				funcionalidadesMantenimiento();
				break;				
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void crearGrupoUsuario() {
		System.out.println(" Ingrese el nombre del grupo a crear: ");
		String nombre = scanner.nextLine().trim();

		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<String> usuariosId = messageController.getUsersMensajes(usuario.getUserId().toString());

		if(usuariosId  == null || usuariosId.isEmpty() ) {
			System.out.println("No hay usuarios para agregar al grupo a crear");
			moduloChatUsuario();
		}
		String finAgregar = null;
		List<String> usuariosAAgregar = new ArrayList<>();
		while(finAgregar == null){
			System.out.println(" Ingrese el usuario que desea añadir al grupo: Si no desea añadir a ninguno mas, ingrese FIN ");
			for(String u : usuariosId) {
				System.out.println(u);
			}
			String ingresa = scanner.nextLine().trim();
			if (ingresa.equals("FIN")){
				finAgregar="FIN";
			}
			else {
				usuariosAAgregar.add(ingresa);
			}
		}

		List<Long> idsLong = usuariosAAgregar.stream()
											.map(Long::parseLong)
											.collect(Collectors.toList());

		GroupDTO grupoACrear = new GroupDTO();
		grupoACrear.setName(nombre);
		grupoACrear.setMemberIds(idsLong);

		groupController.createGroup(grupoACrear);
		System.out.println(" Grupo creado con exito");
	}

	private void crearGrupoMantenimiento() {
		System.out.println(" Ingrese el nombre del grupo a crear: ");
		String nombre = scanner.nextLine().trim();

		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<String> usuariosId = messageController.getUsersMensajes(usuario.getUserId().toString());

		if(usuariosId  == null || usuariosId.isEmpty() ) {
			System.out.println("No hay usuarios para agregar al grupo a crear");
			moduloChatMantenimiento();
		}
		String finAgregar = null;
		List<String> usuariosAAgregar = new ArrayList<>();
		while(finAgregar == null){
			System.out.println(" Ingrese el usuario que desea añadir al grupo: Si no desea añadir a ninguno mas, ingrese FIN ");
			for(String u : usuariosId) {
				System.out.println(u);
			}
			String ingresa = scanner.nextLine().trim();
			if (ingresa.equals("FIN")){
				finAgregar="FIN";
			}
			else {
				usuariosAAgregar.add(ingresa);
			}
		}

		List<Long> idsLong = usuariosAAgregar.stream()
											.map(Long::parseLong)
											.collect(Collectors.toList());

		GroupDTO grupoACrear = new GroupDTO();
		grupoACrear.setName(nombre);
		grupoACrear.setMemberIds(idsLong);

		groupController.createGroup(grupoACrear);
		System.out.println(" Grupo creado con exito");
	}

	private void verMensajesDirectosUsuario() {
		System.out.println(" Seleccione con que usuario quiere ver la conversacion");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<String> usuariosId = messageController.getUsersMensajes(usuario.getUserId().toString());

		if(usuariosId  == null || usuariosId.isEmpty() ) {
			System.out.println("No contiene conversaciones directas para ver.");
			moduloChatUsuario();
		}

		for(String u : usuariosId) {
			System.out.println(u);
		}
		String userSeleccionado = scanner.nextLine().trim();
		List<MessageDTO> mensajes = messageController.getDirect(usuario.getUserId(), Long.parseLong(userSeleccionado));
		for (MessageDTO m: mensajes) {
			System.out.println("remitente: " + m.getSenderId());
			System.out.println("destinatario: " + m.getRecipientId());
			System.out.println("fecha: " + m.getTimestamp());
			System.out.println("contenido: " + m.getContent());
		}
	}

	private void verMensajesDirectosMantenimiento() {
		System.out.println(" Seleccione con que usuario quiere ver la conversacion");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<String> usuariosId = messageController.getUsersMensajes(usuario.getUserId().toString());

		if(usuariosId  == null || usuariosId.isEmpty() ) {
			System.out.println("No contiene conversaciones directas para ver.");
			moduloChatMantenimiento();
		}

		for(String u : usuariosId) {
			System.out.println(u);
		}
		String userSeleccionado = scanner.nextLine().trim();
		List<MessageDTO> mensajes = messageController.getDirect(usuario.getUserId(), Long.parseLong(userSeleccionado));
		for (MessageDTO m: mensajes) {
			System.out.println("remitente: " + m.getSenderId());
			System.out.println("destinatario: " + m.getRecipientId());
			System.out.println("fecha: " + m.getTimestamp());
			System.out.println("contenido: " + m.getContent());
		}
	}	

	private void verMensajesGrupoUsuario() {
		System.out.println(" Seleccione con que grupo quiere ver la conversacion");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<GroupDTO>> todosLosGrupos = groupController.getAllGroupsByMemberId(usuario.getUserId());
		
		List<GroupDTO> grupos = todosLosGrupos.getBody();

		if (grupos == null || grupos.isEmpty()) {
			System.out.println("No contiene grupos en su cuenta.");
			moduloChatUsuario();
		}
		
		for(GroupDTO g : grupos) {
			System.out.println(g.getId()  + " - " + g.getName());
		}

		String grupoSeleccionado = scanner.nextLine().trim();

		List<MessageDTO> mensajes = messageController.getGroupMessages(grupoSeleccionado);
		for (MessageDTO m: mensajes) {
			System.out.println("remitente: " + m.getSenderId());
			System.out.println("destinatario: " + m.getRecipientId());
			System.out.println("fecha: " + m.getTimestamp());
			System.out.println("contenido: " + m.getContent());
		}
	}

	private void verMensajesGrupoMantenimiento() {
		System.out.println(" Seleccione con que grupo quiere ver la conversacion");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<GroupDTO>> todosLosGrupos = groupController.getAllGroupsByMemberId(usuario.getUserId());
		
		List<GroupDTO> grupos = todosLosGrupos.getBody();

		if (grupos == null || grupos.isEmpty()) {
			System.out.println("No contiene grupos en su cuenta.");
			moduloChatMantenimiento();
		}
		
		for(GroupDTO g : grupos) {
			System.out.println(g.getId()  + " - " + g.getName());
		}

		String grupoSeleccionado = scanner.nextLine().trim();

		List<MessageDTO> mensajes = messageController.getGroupMessages(grupoSeleccionado);
		for (MessageDTO m: mensajes) {
			System.out.println("remitente: " + m.getSenderId());
			System.out.println("destinatario: " + m.getRecipientId());
			System.out.println("fecha: " + m.getTimestamp());
			System.out.println("contenido: " + m.getContent());
		}
	}


	private void enviarMensajeDirectoUsuario() {
		System.out.println(" Seleccione a que usuario quiere enviar un mensaje");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<UsuarioResponseDTO> usuariosDTO = usuarioController.getTodosLosUsuarios();
		usuariosDTO.removeIf(u -> u.getEmail().equals(mailAutenticado));
		if(usuariosDTO  == null || usuariosDTO.isEmpty() ) {
			System.out.println("No hay otro usuario con quien chatear");
			moduloChatUsuario();
		}

		for(UsuarioResponseDTO u : usuariosDTO) {
			System.out.println(u.getUserId() + " - " + u.getNombreCompleto());
		}

		String userSeleccionado = scanner.nextLine().trim();
		System.out.println("Ingrese el contenido del mensaje a enviar: ");
		String contenidoMensaje = scanner.nextLine().trim();
		SendDirectRequestDTO mensaje = new SendDirectRequestDTO();
		mensaje.setSenderId(usuario.getUserId());
		mensaje.setRecipientUserId(Long.parseLong(userSeleccionado));
		mensaje.setContent(contenidoMensaje);
		messageController.sendDirect(mensaje);
		System.out.println(" Se ha enviado su mensaje directo");
	}

	private void enviarMensajeDirectoMantenimiento() {
		System.out.println(" Seleccione a que usuario quiere enviar un mensaje");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<UsuarioResponseDTO> usuariosDTO = usuarioController.getTodosLosUsuarios();
		usuariosDTO.removeIf(u -> u.getEmail().equals(mailAutenticado));
		if(usuariosDTO  == null || usuariosDTO.isEmpty() ) {
			System.out.println("No hay otro usuario con quien chatear");
			moduloChatMantenimiento();
		}

		for(UsuarioResponseDTO u : usuariosDTO) {
			System.out.println(u.getUserId() + " - " + u.getNombreCompleto());
		}

		String userSeleccionado = scanner.nextLine().trim();
		System.out.println("Ingrese el contenido del mensaje a enviar: ");
		String contenidoMensaje = scanner.nextLine().trim();
		SendDirectRequestDTO mensaje = new SendDirectRequestDTO();
		mensaje.setSenderId(usuario.getUserId());
		mensaje.setRecipientUserId(Long.parseLong(userSeleccionado));
		mensaje.setContent(contenidoMensaje);
		messageController.sendDirect(mensaje);
		System.out.println(" Se ha enviado su mensaje directo");
	}

	private void enviarMensajeGrupoUsuario() {
		System.out.println(" Seleccione a que grupo quiere enviar un mensaje");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<GroupDTO>> todosLosGrupos = groupController.getAllGroupsByMemberId(usuario.getUserId());
		
		List<GroupDTO> grupos = todosLosGrupos.getBody();

		if (grupos == null || grupos.isEmpty()) {
			System.out.println("No contiene grupos en su cuenta.");
			moduloChatUsuario();
		}
		
		for(GroupDTO g : grupos) {
			System.out.println(g.getId() + " - " + g.getName());
		}

		String grupoSeleccionado = scanner.nextLine().trim();
		System.out.println("Ingrese el contenido del mensaje a enviar: ");
		String contenidoMensaje = scanner.nextLine().trim();

		SendGroupRequestDTO mensaje = new SendGroupRequestDTO();
		mensaje.setSenderId(usuario.getUserId());
		mensaje.setGroupId(grupoSeleccionado);
		mensaje.setContent(contenidoMensaje);

		messageController.sendToGroup(mensaje);
		System.out.println(" Se ha enviado su mensaje al grupo");
	}

	private void enviarMensajeGrupoMantenimiento() {
		System.out.println(" Seleccione a que grupo quiere enviar un mensaje");
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<GroupDTO>> todosLosGrupos = groupController.getAllGroupsByMemberId(usuario.getUserId());
		
		List<GroupDTO> grupos = todosLosGrupos.getBody();

		if (grupos == null || grupos.isEmpty()) {
			System.out.println("No contiene grupos en su cuenta.");
			moduloChatMantenimiento();
		}
		
		for(GroupDTO g : grupos) {
			System.out.println(g.getId() + " - " + g.getName());
		}

		String grupoSeleccionado = scanner.nextLine().trim();
		System.out.println("Ingrese el contenido del mensaje a enviar: ");
		String contenidoMensaje = scanner.nextLine().trim();

		SendGroupRequestDTO mensaje = new SendGroupRequestDTO();
		mensaje.setSenderId(usuario.getUserId());
		mensaje.setGroupId(grupoSeleccionado);
		mensaje.setContent(contenidoMensaje);

		messageController.sendToGroup(mensaje);
		System.out.println(" Se ha enviado su mensaje al grupo");
	}

	private void moduloProcesosUsuario() {
		String opcion;

		System.out.println(" 1. Ver todos los procesos");
		System.out.println(" 2. Solicitar crear un proceso");
		System.out.println(" 3. Regresar al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodosLosProcesos();
				moduloProcesosUsuario();
				break;
			case "2":
				solicitarCrearProceso();
				moduloProcesosUsuario();
				break;
			case "3":
				funcionalidadesUsuario();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void moduloProcesosMantenimiento() {
		String opcion;

		System.out.println(" 1. Ver todos los procesos");
		System.out.println(" 2. Aceptar una solicitud de proceso");
		System.out.println(" 3. Regresar al menu anterior");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodosLosProcesos();
				moduloProcesosMantenimiento();
				break;
			case "2":
				aceptarSolicitudProceso();
				moduloProcesosMantenimiento();
				break;
			case "3":
				funcionalidadesMantenimiento();
				break;
			default:
				System.out.println("Opcion invalida.");
		}
	}

	private void aceptarSolicitudProceso() {
		ResponseEntity<List<ProcessRequestDTO>> listaSolicitudesTodas = processRequestController.getAll();
		List<ProcessRequestDTO> solicitudesTodas = listaSolicitudesTodas.getBody();
		if (solicitudesTodas == null || solicitudesTodas.isEmpty()) {
			System.out.println("No hay solciitudes de procesos creadas.");
			funcionalidadesMantenimiento();
		}
		List<ProcessRequestDTO> solicitudesPendientes = solicitudesTodas.stream()
			.filter(s -> "PENDIENTE".equalsIgnoreCase(s.getStatus()))
			.collect(Collectors.toList());

		if (solicitudesPendientes == null || solicitudesPendientes.isEmpty()) {
			System.out.println("No hay solciitudes de procesos PENDIENTES de aprobar.");
			funcionalidadesMantenimiento();
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

private void ejecutarProceso() {
    ResponseEntity<List<ProcessDTO>> procesosDTO = processController.getAll();
    List<ProcessDTO> procesos = procesosDTO.getBody();

    if (procesos == null || procesos.isEmpty()) {
        System.out.println("No hay procesos creados.");
        funcionalidadesUsuario();
        return; // 👈 importante, si no, sigue y rompe
    }
    
    System.out.println("Seleccione que proceso desea ejecutar (ID del proceso): ");
    for (ProcessDTO p : procesos) {
        System.out.println("----------------------------");
        System.out.println("ID: " + p.getId());              // 👈 mostrás el ID
        System.out.println("Nombre: " + p.getName());
        System.out.println("descripcion : " + p.getDescription());
        System.out.println("tipo: " + p.getProcessType());
        System.out.println("costo: " + p.getCost());
        System.out.println("----------------------------");
    }

    String procesoSeleccionado = scanner.nextLine().trim();

    // 🔹 INICIALIZAR EN NULL
    ProcessDTO processSelect = null;
    for (ProcessDTO p : procesos) {
        // comparamos lo que escribió el usuario con el ID del proceso
        if (String.valueOf(p.getId()).equals(procesoSeleccionado)) {
            processSelect = p;
            break;
        }
    }

    if (processSelect == null) {
        System.out.println("Proceso no encontrado, intente nuevamente.");
        return;
    }

    String city = extractKeyValuePair(processSelect.getDescription(), "city");
    String from = extractKeyValuePair(processSelect.getDescription(), "from");
    String to   = extractKeyValuePair(processSelect.getDescription(), "to");

    if (processSelect.getProcessType().contains("TEMPERATURA")) {
        System.out.println(" Ejecutando proceso para la TEMPERATURA...");
        System.out.println("   Proceso: " + processSelect.getName());
        System.out.println("   Ciudad: " + city);
        System.out.println("   Desde: " + from);
        System.out.println("   Hasta: " + to);
        
        ExecutionHistory resultado = processService.runTemperatureReport(
                city, from, to, processSelect.getId()
        );

        System.out.println("Estado final de la ejecución: " + resultado.getStatus());
    } 
    else if (processSelect.getProcessType().contains("HUMEDAD")) {
        System.out.println("Ejecutando proceso para la HUMEDAD...");
        System.out.println("  Proceso: " + processSelect.getName());
        System.out.println("  Ciudad: " + city);
        System.out.println("  Desde: " + from);
        System.out.println("  Hasta: " + to);
        
        ExecutionHistory resultado  = processService.runHumidityReport(
                city, from, to, processSelect.getId() 
        );

        System.out.println("Estado final de la ejecución: " + resultado.getStatus());
    }
}


	private String extractKeyValuePair(String text, String key) {
        if (text == null || key == null) return null;
        
        String searchPattern = key + "=";
        int keyIndex = text.indexOf(searchPattern);
        if (keyIndex == -1) return null;
        
        // Inicio del valor después de "key="
        int valueStart = keyIndex + searchPattern.length();
        
        // Buscar el final del valor (hasta coma o final de string)
        int valueEnd = text.indexOf(",", valueStart);
        if (valueEnd == -1) {
            valueEnd = text.length(); // Si no hay coma, tomar hasta el final
        }
        
        String value = text.substring(valueStart, valueEnd).trim();
        return value.isEmpty() ? null : value;
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
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		authenticationController.logout(usuario.getEmail());
		System.out.println("Se cerró la sesión del usuario " + usuario.getUserId() + " "+ usuario.getRol() + " con mail: " +usuario.getEmail());
		this.mailAutenticado = null;
		home();
	}
}
