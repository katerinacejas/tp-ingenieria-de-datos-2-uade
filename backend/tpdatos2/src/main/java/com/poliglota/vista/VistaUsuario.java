package com.poliglota.vista;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
import com.poliglota.model.mysql.ExecutionHistory;
import com.poliglota.service.ProcessService;

@Component
public class VistaUsuario extends Vista{
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

	public VistaUsuario (String mailAutenticado, Scanner scanner,
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

	public void home() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println(" 1. Ver catalogo de procesos disponibles");
		System.out.println(" 2. Crear solicitud de proceso");
		System.out.println(" 3. Ver todas mis solicitudes de proceso");
		System.out.println(" 4. Ver/descargar resultados de ejecucion de mis solicitudes");
		System.out.println(" 5. Ver alertas climaticas");
		System.out.println(" 6. Modulo chat privado/grupal");
		System.out.println(" 7. Ver mi cuenta corriente");
		System.out.println(" 8. Ver mis facturas");
		System.out.println(" 9. Pagar una factura pendiente");
		System.out.println(" 10. Ver mis pagos realizados");
		System.out.println(" 11. Depositar dinero en mi cuenta corriente");
		System.out.println(" 12. Ver historial de movimientos de mi cuenta corriente");
		System.out.println(" 13. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verCatalogoProcesosDisponibles();
				home();
				break;
			case "2":
				crearSolicitudProceso();
				home();
				break;
			case "3":
				verMisSolicitudesDeProceso();
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
				verMiCuentaCorriente();
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
				depositarPlata();
				home();
				break;
			case "12":
				
				home();
				break;																							
			case "13":
				cerrarSesion();
				break;
			default:
				System.out.println("Opcion invalida.");
				home();
		}
	}

	private void verCatalogoProcesosDisponibles() {
		vistaGeneral.verCatalogoProcesosDisponibles();
	}

	private void crearSolicitudProceso() {
		ResponseEntity<List<ProcessDTO>> procesosDTO = processController.getAll();
		List<ProcessDTO> procesos = procesosDTO.getBody();

		if (procesos == null || procesos.isEmpty()) {
			System.out.println("No hay procesos creados para solicitar.");
			return;
		}

		System.out.println("===== PROCESOS =====");
		for (ProcessDTO p : procesos) {
			System.out.println("----------------------------");
			System.out.println("Id: " + p.getId());
			System.out.println("Nombre: " + p.getName());
			System.out.println("Descripcion : " + p.getDescription());
			System.out.println("Tipo: " +p.getProcessType());
			System.out.println("Costo: " + p.getCost());
		System.out.println("----------------------------");
		}	
		System.out.println("Seleccione un proceso a solicitar:");
		String idProceso = scanner.nextLine().trim();

		System.out.println("Indique los parametros con los cuales solicitar el proceso");
		
		System.out.println("Ciudad:");
		String ciudad = scanner.nextLine().trim();
		
		System.out.println("Pais: ");
		String pais = scanner.nextLine().trim();

		System.out.println("Fecha de inicio: (formato yyyy-MM-dd)");
		String fechaInicio = scanner.nextLine().trim();

		System.out.println("Fecha de fin: (formato yyyy-MM-dd)");
		String fechaFin = scanner.nextLine().trim();

		System.out.println("De que forma quiere agrupar los datos? ANUAL / MENSUAL");
		String agrupacion = scanner.nextLine().trim();

		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);

		ProcessRequestDTO solicitud = new ProcessRequestDTO();
		solicitud.setUserId(usuario.getUserId().toString());
		solicitud.setProcessId(idProceso);
		solicitud.setStatus("PENDIENTE");
		solicitud.setRequestDate(LocalDateTime.now());
		solicitud.setInvoiceId(null);
		solicitud.setCity(ciudad);
		solicitud.setCountry(pais);
		solicitud.setStartDate(LocalDate.parse(fechaInicio));
		solicitud.setEndDate(LocalDate.parse(fechaFin));
		solicitud.setAgrupacionDeDatos(agrupacion);

		processRequestController.createProcessRequest(solicitud);

		System.out.println("Solcitud de proceso creada correctamente, debe aprobarla y ejecutarla un personal de mantenimiento");	
	}

	private void verMisSolicitudesDeProceso() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<ProcessRequestDTO> lista = processRequestController.getProcessRequestByUser(usuario.getUserId()).getBody();
		
		if (lista == null || lista.isEmpty()) {
			System.out.println("No creo ninguna solicitud de proceso aun.");
			return;
		}

		ProcessDTO p = new ProcessDTO();
		System.out.println("===== MIS SOLICITUDES DE PROCESOS =====");
		for (ProcessRequestDTO s : lista) {
			p = processController.getById(Long.parseLong(s.getProcessId())).getBody();
			System.out.println("----------------------------");
			System.out.println("Nombre proceso: " + p.getName());
			System.out.println("Descripcion proceso: " + p.getDescription());
			System.out.println("Tipo de proceso: " +p.getProcessType());
			System.out.println("Costo: " + p.getCost());
			System.out.println("Estado solicitud: " + s.getStatus());
			System.out.println("Fecha solicitud: " + s.getRequestDate());
			System.out.println("Parametro ciudad: " + s.getCity());
			System.out.println("Parametro pais: " + s.getCountry());
			System.out.println("Parametro fecha inicio: " + s.getStartDate());
			System.out.println("Parametro fecha fin: " + s.getEndDate());
			System.out.println("Parametro agrupacion de datos: " + s.getAgrupacionDeDatos());
		System.out.println("----------------------------");
		}	
	}

	private void verMiCuentaCorriente() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		AccountDTO cuenta = accountController.getAccountByUser(usuario.getUserId()).getBody();
		System.out.println("===== MI CUENTA CORRIENTE =====");
		System.out.println(cuenta.getCurrentBalance());
	}

	private void depositarPlata() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		AccountDTO cuenta = accountController.getAccountByUser(usuario.getUserId()).getBody();
		System.out.println("Ingrese la cantidad de dinero que quiere depositar: ");
		String plata = scanner.nextLine().trim();
		plata = plata.replace(",", ".");
    	double monto = Double.parseDouble(plata);
		accountController.deposit(Long.parseLong(cuenta.getAccountId()),monto);
		System.out.println("Dinero depositado correctamente");
	}

	private void cerrarSesion() {
		String mail = this.mailAutenticado;
		this.mailAutenticado = null;
		vistaGeneral.cerrarSesion(mail);
	}

	



	/*
			************************************************************
			******************** MODULO FACTURAS ***********************
			************************************************************
	*/

	private void moduloFacturas() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		ResponseEntity<List<InvoiceDTO>> facturasDTO = invoiceController.getInvoicesByUser(usuario.getUserId());
		
		List<InvoiceDTO> facturas = facturasDTO.getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No tenés facturas registradas.");
			return;
		}

		else {
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

			System.out.println("===== SELECCIONE UNA OPCION =====");
			System.out.println(" 1. Pagar una factura");
			System.out.println(" 2. Volver al menu anterior");
			opcion = scanner.nextLine().trim();
			switch (opcion) {
				case "1":
					System.out.println("Ingrese el ID de la factura que quiere pagar: ");
					String idFactura = scanner.nextLine().trim();
					pagarUnaFactura(Long.parseLong(idFactura));
					break;
				case "2":
					home();
					break;
				default:
					System.out.println("Opcion invalida.");
					home();
			}
		}
	}

	private void pagarUnaFactura(Long idFactura) {
		invoiceController.updateStatus(idFactura, "PAGADA");
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
		home();
	}
	
	/*
			************************************************************
			********************* MODULO ALERTAS ***********************
			************************************************************
	*/

	private void moduloAlertas() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println(" 1. Ver todas las alertas de mis procesos");
		System.out.println(" 3. Regresar al menu anterior");

		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verTodasLasAlertasDeMisProcesos();
				moduloAlertas();
				break;
			case "3":
				home();
				break;
			default:
				System.out.println("Opcion invalida.");
				moduloAlertas();
		}
	}

	private void verTodasLasAlertasDeMisProcesos() {

		/*
				rework para que sean las alertas ed unicamente sus procesos
		*/
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

	/*
			************************************************************
			***************** MODULO CUENTA CORRIENTE ******************
			************************************************************
	*/		
	
	private void moduloCuentaCorriente() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		AccountDTO cuenta = accountController.getAccountByUser(usuario.getUserId()).getBody();
		System.out.println("===== MI CUENTA CORRIENTE =====");
		System.out.println(cuenta.getCurrentBalance());

		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println("1. Depositar plata ");
		System.out.println("2. Regresar al menu anterior ");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				depositarPlata();
				break;
			case "2":
				home();
				break;
			default:
				System.out.println("Opcion invalida.");
				moduloCuentaCorriente();
		}
	}


}