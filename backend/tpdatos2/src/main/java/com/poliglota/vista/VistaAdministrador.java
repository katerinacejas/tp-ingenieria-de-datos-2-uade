package com.poliglota.vista;

import java.util.Scanner;
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

@Component
public class VistaAdministrador extends Vista{
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

	public VistaAdministrador (String mailAutenticado, Scanner scanner,
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
		System.out.println(" 1. Ver todos los usuarios");
		System.out.println(" 2. Registrar personal de mantenimiento");
		System.out.println(" 3. Registrar otro administrador");
		System.out.println(" 4. Ver todos los sensores");
		System.out.println(" 5. Ver historial de controles de funcionamiento de sensores");
		System.out.println(" 6. Ver catalogo de procesos disponibles");
		System.out.println(" 7. Crear nuevo proceso");
		System.out.println(" 8. Modificar un proceso");
		System.out.println(" 9. Ver solicitudes de procesos de todos los usuarios");
		System.out.println(" 10. Ver historial de ejecuciones de procesos");
		System.out.println(" 11. Ver todas las alertas");
		System.out.println(" 12. Ver historial de ejecuciones de procesos");
		System.out.println(" 13. Ver facturas pendientes de pago de usuarios");
		System.out.println(" 14. Ver todas las facturas de usuarios");
		System.out.println(" 15. Ver total facturado y total deuda");
		System.out.println(" 16. Cerrar sesion");
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
				verCatalogoProcesosDisponibles();
				home();
				break;
			case "7":
				
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
				
				home();
				break;																																												
			case "16":
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

	private void cerrarSesion() {
		String mail = this.mailAutenticado;
		this.mailAutenticado = null;
		vistaGeneral.cerrarSesion(mail);
	}
}
