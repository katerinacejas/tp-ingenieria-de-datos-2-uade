package com.poliglota.service;

import com.poliglota.DTO.InvoiceDTO;
import com.poliglota.model.mysql.Invoice;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.InvoiceRepository;
import com.poliglota.repository.ProcessRequestRepository;
import com.poliglota.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
	private final ProcessRequestRepository processRequestRepository;

    //  Obtener todas las facturas
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll()
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Obtener factura por ID
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + id));
        return toDto(invoice);
    }

    //  Obtener facturas por usuario
    public List<InvoiceDTO> getInvoicesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
        return invoiceRepository.findByUser(user)
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Obtener facturas por estado
    public List<InvoiceDTO> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status)
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Crear factura nueva
    public InvoiceDTO createInvoice(Long userId, List<ProcessRequest> processes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setBilledProcesses(processes);
        invoice.setStatus("pendiente");

		for (ProcessRequest process : processes) {
			process.setInvoice(invoice);
			processRequestRepository.save(process);
		}
		
        return toDto(invoiceRepository.save(invoice));
    }

    //  Cambiar estado de factura
    public InvoiceDTO updateStatus(Long id, String newStatus) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + id));
        invoice.setStatus(newStatus);
        return toDto(invoiceRepository.save(invoice));
    }

	private InvoiceDTO toDto(Invoice invoice) {
		InvoiceDTO dto = new InvoiceDTO();
		dto.setInvoiceId(invoice.getInvoiceId().toString());
		dto.setUserId(invoice.getUser().getUserId().toString());
		dto.setIssueDate(invoice.getIssueDate());
		dto.setBilledProcessesIds(invoice.getBilledProcesses().stream()
				.map(pr -> pr.getProcessRequestId())
				.toList());
		dto.setStatus(invoice.getStatus());
		return dto;
	}
}
