package com.poliglota.service;

import com.poliglota.model.mysql.Payment;
import com.poliglota.repository.jpa.InvoiceRepository;
import com.poliglota.repository.jpa.PaymentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.poliglota.DTO.PaymentDTO;
import com.poliglota.model.mysql.Invoice;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    //  Obtener todos los pagos
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll()
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Obtener pago por ID
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));
		return toDto(payment);
	}

    //  Obtener pagos por factura
    public List<PaymentDTO> getPaymentsByInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + invoiceId));
        return paymentRepository.findByInvoice(invoice)
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Obtener pagos por método
    public List<PaymentDTO> getPaymentsByMethod(String paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod)
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Buscar pagos entre fechas
    public List<PaymentDTO> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end)
			.stream()
			.map(this::toDto)
			.toList();
    }

    //  Registrar un nuevo pago
    public PaymentDTO registerPayment(Long invoiceId, String paymentMethod) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + invoiceId));

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(invoice.calculateTotalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDateTime.now());

        invoice.setStatus("pagada");
        invoiceRepository.save(invoice);

        return toDto(paymentRepository.save(payment));
    }

	private PaymentDTO toDto(Payment payment) {
		PaymentDTO dto = new PaymentDTO();
		dto.setPaymentId(payment.getPaymentId().toString());
		dto.setInvoiceId(payment.getInvoice().getInvoiceId().toString());
		dto.setPaymentDate(payment.getPaymentDate());
		dto.setAmount(payment.getAmount());
		dto.setPaymentMethod(payment.getPaymentMethod());
		return dto;
	}

}
