package com.poliglota.service;

import com.poliglota.model.mysql.Payment;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.jpa.InvoiceRepository;
import com.poliglota.repository.jpa.PaymentRepository;
import com.poliglota.repository.jpa.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.poliglota.DTO.PaymentDTO;
import com.poliglota.DTO.response.UsuarioResponseDTO;
import com.poliglota.model.mysql.Invoice;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
	private final UserRepository userRepository;

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll()
			.stream()
			.map(this::toDto)
			.toList();
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));
		return toDto(payment);
	}

	public List<PaymentDTO> getPaymentsByUser(Long userId) {
		User usuario = userRepository.findById(userId).orElse(null);
		List<Invoice> facturas = invoiceRepository.findByUserAndStatus(usuario, "PAGADA");
		List<Payment> pagos = new ArrayList<>();
		for(Invoice fact: facturas){
			pagos.add(paymentRepository.findByInvoice(fact));
		}
		return pagos.stream().map(pago -> toDto(pago)).toList();
	}

    public List<PaymentDTO> getPaymentsByMethod(String paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod)
			.stream()
			.map(this::toDto)
			.toList();
    }

    public List<PaymentDTO> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end)
			.stream()
			.map(this::toDto)
			.toList();
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
