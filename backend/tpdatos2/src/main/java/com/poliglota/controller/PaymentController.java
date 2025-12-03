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

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(  Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/method/{paymentMethod}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByMethod(  String paymentMethod) {
        return ResponseEntity.ok(paymentService.getPaymentsByMethod(paymentMethod));
    }

    @GetMapping("/between")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByDateRange(
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(paymentService.getPaymentsByDateRange(start, end));
    }

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<PaymentDTO>> getPaymentsByUser(Long userId) {
		return ResponseEntity.ok(paymentService.getPaymentsByUser(userId));
	}

}
