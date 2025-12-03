package com.poliglota.service;

import com.poliglota.DTO.InvoiceDTO;
import com.poliglota.model.mysql.Invoice;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.model.mysql.Payment;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.jpa.InvoiceRepository;
import com.poliglota.repository.jpa.PaymentRepository;
import com.poliglota.repository.jpa.ProcessRequestRepository;
import com.poliglota.repository.jpa.UserRepository;

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
	private final PaymentRepository paymentRepository;

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll()
			.stream()
			.map(this::toDto)
			.toList();
    }

    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + id));
        return toDto(invoice);
    }

    public List<InvoiceDTO> getInvoicesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
        return invoiceRepository.findByUser(user)
			.stream()
			.map(this::toDto)
			.toList();
    }

	public List<InvoiceDTO> getInvoicesByUserByStatus(Long userId, String status) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
        return invoiceRepository.findByUserAndStatus(user, status)
				.stream()
				.map(this::toDto)
				.toList();
	}

    public List<InvoiceDTO> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status)
			.stream()
			.map(this::toDto)
			.toList();
    }

    public InvoiceDTO createInvoice(Long userId, ProcessRequest processRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setBilledProcessRequest(processRequest);
        invoice.setStatus("PENDIENTE");

		processRequest.setInvoice(invoice);
		processRequestRepository.save(processRequest);

        return toDto(invoiceRepository.save(invoice));
    }

    public InvoiceDTO pagarFactura(Long id, double monto, String metodoPago) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + id));
        invoice.setStatus("PAGADA");

		Payment payment = new Payment();
		payment.setInvoice(invoice);
		payment.setPaymentDate(LocalDateTime.now());
		payment.setAmount(monto);
		payment.setPaymentMethod(metodoPago);
		paymentRepository.save(payment);

        return toDto(invoiceRepository.save(invoice));
    }

    private InvoiceDTO toDto(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getInvoiceId().toString());
        dto.setUserId(invoice.getUser().getUserId().toString());
        dto.setIssueDate(invoice.getIssueDate());
        dto.setBilledProcessRequest(invoice.getBilledProcessRequest().getProcessRequestId().toString());
        dto.setStatus(invoice.getStatus());
        return dto;
    }
}
