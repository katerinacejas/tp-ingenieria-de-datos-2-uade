package com.poliglota.service;

import com.poliglota.model.mysql.Payment;
import com.poliglota.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.poliglota.model.mysql.Invoice;
import com.poliglota.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    //  Obtener todos los pagos
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    //  Obtener pago por ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));
    }

    //  Obtener pagos por factura
    public List<Payment> getPaymentsByInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + invoiceId));
        return paymentRepository.findByInvoice(invoice);
    }

    //  Obtener pagos por método
    public List<Payment> getPaymentsByMethod(String paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    //  Buscar pagos entre fechas
    public List<Payment> getPaymentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    //  Registrar un nuevo pago
    public Payment registerPayment(Long invoiceId, double amount, String paymentMethod) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + invoiceId));

        // Validar monto
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }

        // Crear el pago
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDateTime.now());

        // Actualizar estado de la factura (opcional)
        invoice.setStatus("pagada");
        invoiceRepository.save(invoice);

        return paymentRepository.save(payment);
    }

    //  Eliminar pago
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

}
