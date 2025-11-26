package com.poliglota.controller;

import com.poliglota.DTO.PaymentDTO;
import com.poliglota.model.mysql.Payment;
import com.poliglota.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //  Obtener todos los pagos
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    //  Obtener pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(  Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    //  Obtener pagos por factura
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByInvoice(  Long invoiceId) {
        return ResponseEntity.ok(paymentService.getPaymentsByInvoice(invoiceId));
    }

    //  Obtener pagos por método
    @GetMapping("/method/{paymentMethod}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByMethod(  String paymentMethod) {
        return ResponseEntity.ok(paymentService.getPaymentsByMethod(paymentMethod));
    }

    //  Obtener pagos entre fechas
    @GetMapping("/between")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByDateRange(
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(paymentService.getPaymentsByDateRange(start, end));
    }

    //  Registrar nuevo pago
    @PostMapping
    public ResponseEntity<PaymentDTO> registerPayment(
               Long invoiceId,
               String paymentMethod) {
        return ResponseEntity.ok(paymentService.registerPayment(invoiceId, paymentMethod));
    }

}
