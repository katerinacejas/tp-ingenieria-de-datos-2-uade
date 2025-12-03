package com.poliglota.vista;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.poliglota.DTO.AccountDTO;
import com.poliglota.DTO.AlertsDTO;
import com.poliglota.DTO.GroupDTO;
import com.poliglota.DTO.InvoiceDTO;
import com.poliglota.DTO.MessageDTO;
import com.poliglota.DTO.request.LoginRequestDTO;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.request.SendDirectRequestDTO;
import com.poliglota.DTO.request.SendGroupRequestDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.DTO.ProcessDTO;
import com.poliglota.DTO.ProcessRequestDTO;
import com.poliglota.DTO.SensorDTO;

import com.poliglota.controller.AccountController;
import com.poliglota.controller.AccountMovementHistoryController;
import com.poliglota.controller.AlertsController;
import com.poliglota.controller.AuthenticationController;
import com.poliglota.controller.ExecutionHistoryController;
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
import com.poliglota.controller.SessionController;

import com.poliglota.service.ProcessService;

@Component
public class VistaCompartida extends Vista{
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
	private final ExecutionHistoryController executionHistoryController;
	private final SessionController sessionController;
	private final Scanner scanner;

	public VistaCompartida(
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
			ExecutionHistoryController executionHistoryController,
			SessionController sessionController) {
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
		this.executionHistoryController = executionHistoryController;
		this.sessionController = sessionController;
        this.scanner = new Scanner(System.in);
    }

	public void start() {
		// registro del usuario admin
		RegistroRequestDTO registro = new RegistroRequestDTO();
		registro.setEmail("admin@admin.com");
		registro.setNombreCompleto("admin");
		registro.setPassword("Test1234");
		registro.setRol("ADMIN");
		ResponseEntity<String> response = authenticationController.registerAdmin(registro);
		if (response != null && "ok".equals(response.getBody())) {
			System.out.println("usuario administrador por default generado OK");
		}
		home();
	}

	@Override
	public void home() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
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
				    System.out.println("=============================================\n\n");
				home();
		}
	}

	private void registrarse() {
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
		registro.setRol("USUARIO");

		ResponseEntity<String> response = authenticationController.register(registro);
		if (response != null && "ok".equals(response.getBody())) {
			System.out.println("Registro exitoso, puede iniciar sesion");
			  System.out.println("=============================================\n\n");
			home();
		}
		else {
			System.out.println("ocurrio un error, vuelva a registrarse");
			registrarse();
		}
	}

	private void iniciarSesion() {
		System.out.println("Email: ");
		String email = scanner.nextLine().trim();

		System.out.println("Contraseña: ");
		String contrasenia = scanner.nextLine().trim();

		LoginRequestDTO login = new LoginRequestDTO();
		login.setEmail(email);
		login.setPassword(contrasenia);

		ResponseEntity<String> response = authenticationController.login(login);

		System.out.println("respuesta del autenticador controller: " + response.getBody());
		
		if (response != null && "USUARIO".equals(response.getBody())) {
			System.out.println("Inicio de sesion exitoso, rol USUARIO");
			VistaUsuario vistaUsuario = new VistaUsuario(email, scanner,
				accountController, 
				accountMovementHistoryController,
				alertsController,
				authenticationController,
				groupController,
				invoiceController,
				maintenanceCheckController,
				measurementController,
				messageController,
				paymentController,
				processController,
				processRequestController,
				sensorController,
				usuarioController,
				processService,
				this,
				executionHistoryController
			);
			  System.out.println("=============================================\n\n");
			vistaUsuario.home();
		}
		if (response != null && "MANTENIMIENTO".equals(response.getBody())) {
			System.out.println("Inicio de sesion exitoso, rol MANTENIMIENTO");
			VistaMantenimiento vistaMantenimiento = new VistaMantenimiento(email, scanner,
				accountController, 
				accountMovementHistoryController,
				alertsController,
				authenticationController,
				groupController,
				invoiceController,
				maintenanceCheckController,
				measurementController,
				messageController,
				paymentController,
				processController,
				processRequestController,
				sensorController,
				usuarioController,
				processService,
				this,
				executionHistoryController
			);
			  System.out.println("=============================================\n\n");
			vistaMantenimiento.home();
		}
		if (response != null && "ADMIN".equals(response.getBody())) {
			System.out.println("Inicio de sesion exitoso, rol MANTENIMIENTO");
			VistaAdministrador vistaAdministrador = new VistaAdministrador(email, scanner,
				accountController, 
				accountMovementHistoryController,
				alertsController,
				authenticationController,
				groupController,
				invoiceController,
				maintenanceCheckController,
				measurementController,
				messageController,
				paymentController,
				processController,
				processRequestController,
				sensorController,
				usuarioController,
				processService,
				this,
				sessionController	
			);
			  System.out.println("=============================================\n\n");
			vistaAdministrador.home();
		}
		if (response != null && "Error".equals(response.getBody())) {
			System.out.println("Ocurrio un error (exception), inicie nuevamente");
			iniciarSesion();
		}
	}

	public void moduloChat(String mailAutenticado, Vista vista) {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);

		String opcion;

		if (usuario.getRol().equals("USUARIO")){
			System.out.println(" 1. Ver los mensajes de una conversación directa");
			System.out.println(" 2. Ver los mensajes de un grupo");
			System.out.println(" 3. Enviar mensaje directo");
			System.out.println(" 4. Enviar mensaje a un grupo");
			System.out.println(" 5. Regresar al menu anterior");

			opcion = scanner.nextLine().trim();
			switch (opcion) {
				case "1":
					verMensajesDirectos(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;
				case "2":
					verMensajesGrupo(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;
				case "3":
					enviarMensajeDirecto(mailAutenticado);

					moduloChat(mailAutenticado, vista);
					break;
				case "4":
					enviarMensajeGrupo(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;
				case "5":
					System.out.println("=============================================\n\n");
					vista.home();
					break;				
				default:
					System.out.println("Opcion invalida.");
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
			}
		}
		else {
			System.out.println(" 1. Ver los mensajes de una conversación directa");
			System.out.println(" 2. Ver los mensajes de un grupo");
			System.out.println(" 3. Enviar mensaje directo");
			System.out.println(" 4. Enviar mensaje a un grupo");
			System.out.println(" 5. Crear grupo");
			System.out.println(" 6. Regresar al menu anterior");

			opcion = scanner.nextLine().trim();
			switch (opcion) {
				case "1":
					verMensajesDirectos(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;
				case "2":
					verMensajesGrupo(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;
				case "3":
					enviarMensajeDirecto(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;
				case "4":
					enviarMensajeGrupo(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;
				case "5":
					crearGrupo(mailAutenticado);
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
					break;			
				case "6":
					System.out.println("=============================================\n\n");
					vista.home();
					break;			
				default:
					System.out.println("Opcion invalida.");
					System.out.println("=============================================\n\n");
					moduloChat(mailAutenticado, vista);
			}
		}
	}

	private void crearGrupo(String mailAutenticado) {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<UsuarioResponseDTO> usuarios = usuarioController.getUsuariosYMantenimiento();

		if(usuarios  == null || usuarios.isEmpty() ) {
			System.out.println("No hay usuarios suficientes para crear un grupo");
			return;
		}

		System.out.println(" Ingrese el nombre del grupo a crear: ");
		String nombre = scanner.nextLine().trim();

		String finAgregar = null;
		List<String> usuariosAAgregar = new ArrayList<>();
		while(finAgregar == null){
			System.out.println(" Ingrese el usuario que desea añadir al grupo: Si no desea añadir a ninguno mas, ingrese FIN ");
			for(UsuarioResponseDTO u : usuarios) {
				System.out.println("ID " + u.getUserId() + " - " + u.getNombreCompleto());
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

	private void verMensajesDirectos(String mailAutenticado) {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<String> usuariosId = messageController.getUsersMensajes(usuario.getUserId().toString());

		if(usuariosId  == null || usuariosId.isEmpty() ) {
			System.out.println("No contiene conversaciones directas para ver.");
			return;
		}

		System.out.println(" Seleccione con que usuario quiere ver la conversacion");
		for(String u : usuariosId) {
			UsuarioResponseDTO usuarioInterac = usuarioController.getUsuarioPorId(Long.parseLong(u));
			System.out.println(usuarioInterac.getUserId() + " - " + usuarioInterac.getNombreCompleto());
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

	private void verMensajesGrupo(String mailAutenticado) {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<GroupDTO>> todosLosGrupos = groupController.getAllGroupsByMemberId(usuario.getUserId());
		
		List<GroupDTO> grupos = todosLosGrupos.getBody();

		if (grupos == null || grupos.isEmpty()) {
			System.out.println("No contiene grupos en su cuenta.");
			return;
		}
		
		System.out.println(" Seleccione con que grupo quiere ver la conversacion");
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

	private void enviarMensajeDirecto(String mailAutenticado) {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<UsuarioResponseDTO> usuariosDTO = usuarioController.getUsuariosYMantenimiento();
		usuariosDTO.removeIf(u -> u.getEmail().equals(mailAutenticado));
		
		if(usuariosDTO  == null || usuariosDTO.isEmpty() ) {
			System.out.println("No hay otro usuario con quien chatear");
			return;
		}

		System.out.println(" Seleccione a que usuario quiere enviar un mensaje");
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

	private void enviarMensajeGrupo(String mailAutenticado) {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<GroupDTO>> todosLosGrupos = groupController.getAllGroupsByMemberId(usuario.getUserId());
		
		List<GroupDTO> grupos = todosLosGrupos.getBody();

		if (grupos == null || grupos.isEmpty()) {
			System.out.println("No contiene grupos en su cuenta.");
			return;
		}
		
		System.out.println(" Seleccione a que grupo quiere enviar un mensaje");
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

	public void verCatalogoProcesosDisponibles(){
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
			System.out.println("Descripcion : " + p.getDescription());
			System.out.println("Tipo: " +p.getProcessType());
			System.out.println("Costo: " + p.getCost());
		System.out.println("----------------------------");
		}		
	}

	public void verTodosLosSensores() {
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

	public void cerrarSesion(String mailAutenticado) {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		authenticationController.logout(usuario.getEmail());
		System.out.println("Se cerró la sesión del usuario " + usuario.getUserId() + " "+ usuario.getRol() + " con mail: " +usuario.getEmail());
		home();
	}
}
