package com.poliglota.vista;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.poliglota.DTO.AccountDTO;
import com.poliglota.DTO.AlertsDTO;
import com.poliglota.DTO.ExecutionHistoryDTO;
import com.poliglota.DTO.InvoiceDTO;
import com.poliglota.DTO.AccountMovementHistoryDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.DTO.PaymentDTO;
import com.poliglota.DTO.ProcessDTO;
import com.poliglota.DTO.ProcessRequestDTO;
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
import com.poliglota.service.ProcessService;

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
	private final ExecutionHistoryController executionHistoryController;

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
		VistaCompartida vistaGeneral,
		ExecutionHistoryController executionHistoryController
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
		this.executionHistoryController = executionHistoryController;
	}

	public void home() {
		String opcion;

		System.out.println("===== SELECCIONE UNA OPCION =====");
		System.out.println(" 1. Ver catalogo de procesos disponibles");
		System.out.println(" 2. Crear solicitud de proceso");
		System.out.println(" 3. Ver todas mis solicitudes de proceso");
		System.out.println(" 4. Ver resultados de ejecucion de mis solicitudes");
		System.out.println(" 5. Ver alertas climaticas activas");
		System.out.println(" 6. Ver alertas climaticas resueltas");
		System.out.println(" 7. Modulo chat privado/grupal");
		System.out.println(" 8. Ver mi cuenta corriente");
		System.out.println(" 9. Ver mis facturas pendientes");
		System.out.println(" 10. Ver mis facturas pagadas");
		System.out.println(" 11. Pagar una factura pendiente");
		System.out.println(" 12. Ver mis pagos realizados");
		System.out.println(" 13. Depositar dinero en mi cuenta corriente");
		System.out.println(" 14. Ver historial de movimientos de mi cuenta corriente");
		System.out.println(" 15. Cerrar sesion");
		opcion = scanner.nextLine().trim();
		switch (opcion) {
			case "1":
				verCatalogoProcesosDisponibles();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "2":
				crearSolicitudProceso();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "3":
				verMisSolicitudesDeProceso();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "4":
				verResultadosDeSolicitudes();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "5":
				verAlertasClimaticasActivas();
				    System.out.println("=============================================\n\n");
				home();
				break;	
			case "6":
				verAlertasClimaticasResueltas();
				    System.out.println("=============================================\n\n");
				home();
				break;								
			case "7":
				vistaGeneral.moduloChat(mailAutenticado, this);
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "8":
				verMiCuentaCorriente();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "9":
				verMisFacturasPendientes();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "10":
				verMisFacturasPagadas();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "11":
				pagarUnaFactura();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "12":
				verMisPagosRealizados();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "13":
				depositarPlata();
				    System.out.println("=============================================\n\n");
				home();
				break;
			case "14":
				verHistorialDeMovimientos();
				    System.out.println("=============================================\n\n");
				home();
				break;																							
			case "15":
				cerrarSesion();
				    System.out.println("=============================================\n\n");
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

	private void verResultadosDeSolicitudes() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<ProcessRequestDTO> solicitudesEjecutadas = processRequestController.getProcessRequestByUserAndStatus(usuario.getUserId(), "COMPLETADA").getBody();

		if (solicitudesEjecutadas == null || solicitudesEjecutadas.isEmpty()) {
			System.out.println("No hay ejecucion de ninguna solicitud.");
			return;
		}

		ProcessDTO p = new ProcessDTO();
		System.out.println("===== SOLICITUDES DE PROCESOS EJECUTADAS =====");
		for (ProcessRequestDTO s : solicitudesEjecutadas) {
			p = processController.getById(Long.parseLong(s.getProcessId())).getBody();
			System.out.println("----------------------------");
			System.out.println("ID de solicitud de proceso ejecutada: " + s.getProcessRequestId());
			System.out.println("Nombre proceso: " + p.getName());
			System.out.println("Descripcion proceso: " + p.getDescription());
			System.out.println("Tipo de proceso: " +p.getProcessType());
			System.out.println("Estado solicitud: " + s.getStatus());
			System.out.println("Fecha solicitud: " + s.getRequestDate());
			System.out.println("Parametro ciudad: " + s.getCity());
			System.out.println("Parametro pais: " + s.getCountry());
			System.out.println("Parametro fecha inicio: " + s.getStartDate());
			System.out.println("Parametro fecha fin: " + s.getEndDate());
			System.out.println("Parametro agrupacion de datos: " + s.getAgrupacionDeDatos());
		}	

		System.out.println("Seleccione una solicitud de proceso ejecutada para ver su resultado:");
		String processRequestId = scanner.nextLine().trim();

		List<ExecutionHistoryDTO> ejecuciones = executionHistoryController.getExecutionHistoryByProcessRequestId(processRequestId).getBody();
		System.out.println("===== RESULTADOS DE LA SOLICITUD " + processRequestId + " =====");
		for(ExecutionHistoryDTO e : ejecuciones) {
			System.out.println("----------------------------");
			System.out.println("Fecha de ejecucion: " + e.getExecutionDate());
			System.out.println("Estado de ejecucion: " + e.getStatus());
			System.out.println("Resultado: " + e.getResult());
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

	private void verAlertasClimaticasActivas() {
		List<AlertsDTO> alertas = alertsController.getAlertsByState("ACTIVA").getBody();

		if (alertas == null || alertas.isEmpty()) {
			System.out.println("No hay alertas activas.");
			return;
		}

		System.out.println("===== ALERTAS ACTIVAS =====");
		SensorDTO sensor;
		for (AlertsDTO a : alertas) {
			sensor = sensorController.getById(a.getSensorId()).getBody();
			System.out.println("----------------------------");
			System.out.println("Tipo de alerta: " + a.getType());
			System.out.println("Estado: " + a.getState());
			System.out.println("Sensor: " + sensor.getName());
			System.out.println("Ciudad del sensor: " + sensor.getCity());
			System.out.println("Pais del sensor: " + sensor.getCountry());
			System.out.println("Fecha alerta: " + a.getDatetime());
			System.out.println("Descripcion alerta: " + a.getDescripction());
		}
	}

	private void verAlertasClimaticasResueltas() {
		List<AlertsDTO> alertas = alertsController.getAlertsByState("RESUELTA").getBody();

		if (alertas == null || alertas.isEmpty()) {
			System.out.println("No hay alertas resueltas.");
			return;
		}

		System.out.println("===== ALERTAS RESUELTAS =====");
		SensorDTO sensor;
		for (AlertsDTO a : alertas) {
			sensor = sensorController.getById(a.getSensorId()).getBody();
			System.out.println("----------------------------");
			System.out.println("Tipo de alerta: " + a.getType());
			System.out.println("Estado: " + a.getState());
			System.out.println("Sensor: " + sensor.getName());
			System.out.println("Ciudad del sensor: " + sensor.getCity());
			System.out.println("Pais del sensor: " + sensor.getCountry());
			System.out.println("Fecha alerta: " + a.getDatetime());
			System.out.println("Descripcion alerta: " + a.getDescripction());
		}

	}

	private void verMisFacturasPendientes() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<InvoiceDTO> facturas = invoiceController.getInvoicesByUserByStatus(usuario.getUserId(), "PENDIENTE").getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No tenes facturas pendientes de pago.");
			return;
		}

		System.out.println("===== FACTURAS PENDIENTES DE PAGO =====");
		ProcessRequestDTO prdto;
		ProcessDTO pdto;
		for (InvoiceDTO f : facturas) {
			prdto = processRequestController.getProcessRequestById(Long.parseLong(f.getBilledProcessRequest())).getBody();
			pdto = processController.getById(Long.parseLong(prdto.getProcessId())).getBody();
			System.out.println("----------------------------");
			System.out.println("Fecha: " + f.getIssueDate());
			System.out.println("Estado de la factura: " + f.getStatus());
			System.out.println("Estado de la solicitud de proceso: " + prdto.getStatus());
			System.out.println("Nombre del proceso solicitado: " + pdto.getName());
			System.out.println("Descripcion del proceso solicitado: " + pdto.getDescription());
			System.out.println("Importe a pagar: " + pdto.getCost());
		}
	}

	private void verMisFacturasPagadas() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<InvoiceDTO> facturas = invoiceController.getInvoicesByUserByStatus(usuario.getUserId(), "PAGADA").getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No tenes facturas pagadas.");
			return;
		}

		System.out.println("===== FACTURAS PAGADAS =====");
		ProcessRequestDTO prdto;
		ProcessDTO pdto;
		for (InvoiceDTO f : facturas) {
			prdto = processRequestController.getProcessRequestById(Long.parseLong(f.getBilledProcessRequest())).getBody();
			pdto = processController.getById(Long.parseLong(prdto.getProcessId())).getBody();
			System.out.println("----------------------------");
			System.out.println("Fecha: " + f.getIssueDate());
			System.out.println("Estado de la factura: " + f.getStatus());
			System.out.println("Estado de la solicitud de proceso: " + prdto.getStatus());
			System.out.println("Nombre del proceso solicitado: " + pdto.getName());
			System.out.println("Descripcion del proceso solicitado: " + pdto.getDescription());
			System.out.println("Importe pagado: " + pdto.getCost());
		}
	}	

	private void pagarUnaFactura() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<InvoiceDTO> facturas = invoiceController.getInvoicesByUserByStatus(usuario.getUserId(), "PENDIENTE").getBody();

		if (facturas == null || facturas.isEmpty()) {
			System.out.println("No podes pagar ninguna factura porque no tenes facturas pendientes de pago.");
			return;
		}

		System.out.println("===== FACTURAS PENDIENTES DE PAGO =====");
		ProcessRequestDTO prdto;
		ProcessDTO pdto;
		for (InvoiceDTO f : facturas) {
			prdto = processRequestController.getProcessRequestById(Long.parseLong(f.getBilledProcessRequest())).getBody();
			pdto = processController.getById(Long.parseLong(prdto.getProcessId())).getBody();
			System.out.println("----------------------------");
			System.out.println("ID Factura: " + f.getInvoiceId());
			System.out.println("Fecha: " + f.getIssueDate());
			System.out.println("Estado de la factura: " + f.getStatus());
			System.out.println("Estado de la solicitud de proceso: " + prdto.getStatus());
			System.out.println("Nombre del proceso solicitado: " + pdto.getName());
			System.out.println("Descripcion del proceso solicitado: " + pdto.getDescription());
			System.out.println("Importe a pagar: " + pdto.getCost());
		}

		System.out.println("Selecciona el ID de la factura que quieras pagar");
		String idFactura = scanner.nextLine().trim();

		InvoiceDTO facturaSeleccionada = facturas.stream().filter(f -> f.getInvoiceId().equals(idFactura)).findFirst().orElse(null);
		ProcessRequestDTO processRequestSeleccionada = processRequestController.getProcessRequestById(Long.parseLong(facturaSeleccionada.getBilledProcessRequest())).getBody();
		ProcessDTO procesoSeleccionado = processController.getById(Long.parseLong(processRequestSeleccionada.getProcessId())).getBody();
		AccountDTO cuenta = accountController.getAccountByUser(usuario.getUserId()).getBody();

		if(cuenta.getCurrentBalance() >= procesoSeleccionado.getCost()) {
			invoiceController.pagarFactura(Long.parseLong(idFactura), procesoSeleccionado.getCost(), "PAGO_MANUAL");
			accountController.withdraw(Long.parseLong(cuenta.getAccountId()), procesoSeleccionado.getCost());
			System.out.println("Factura" + idFactura + "pagada correctamente, se debitaron $" + procesoSeleccionado.getCost() + "de tu cuenta.");
		}
		else {
			System.out.println("No tenes suficiente dinero en tu cuenta para pagar esta factura");
			System.out.println("En tu cuenta corriente tenes: $" + cuenta.getCurrentBalance() );
			System.out.println("El importe a abonar en la factura es de: $" + procesoSeleccionado.getCost() );
			System.out.println("Deposita dinero en la cuenta primero");
		}
	}
	
	private void verMisPagosRealizados() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		List<PaymentDTO> pagos = paymentController.getPaymentsByUser(usuario.getUserId()).getBody();

		if (pagos == null || pagos.isEmpty()) {
			System.out.println("No realizaste ningun pago todavia.");
			return;
		}

		System.out.println("===== MIS PAGOS REALIZADOS =====");
		for (PaymentDTO p : pagos) {
			System.out.println("----------------------------");
			System.out.println("Fecha de pago: " + p.getPaymentDate());
			System.out.println("Monto pagado: " + p.getAmount());
			System.out.println("Metodo de pago: " + p.getPaymentMethod());
			System.out.println("Factura: " + p.getInvoiceId());
		}
	}

	private void verHistorialDeMovimientos() {
		UsuarioResponseDTO usuario = usuarioController.getUsuarioPorMail(mailAutenticado);
		AccountDTO account = accountController.getAccountByUser(usuario.getUserId()).getBody();

		List<AccountMovementHistoryDTO> movimientos = accountMovementHistoryController.getMovementsByAccount(Long.parseLong(account.getAccountId())).getBody();
		
		if (movimientos == null || movimientos.isEmpty()) {
			System.out.println("No realizaste ningun movimento en tu cuenta corriente (ni deposito ni debito).");
			return;
		}

		System.out.println("===== MOVIMIENTOS DE MI CUENTA CORRIENTE =====");
		for (AccountMovementHistoryDTO am : movimientos) {
			System.out.println("----------------------------");
			System.out.println("Fecha del movimiento: " + am.getMovementDate());
			System.out.println("Tipo de movimiento: " + am.getMovementType());
			System.out.println("Monto: " + am.getAmount());
			System.out.println("Saldo en mi cuenta antes del movimiento: " + am.getBalanceBeforeMovement());
			System.out.println("Saldo en mi cuenta despues del movimiento: " + am.getBalanceAfterMovement());
		}
	}


}