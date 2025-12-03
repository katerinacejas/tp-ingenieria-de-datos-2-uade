package com.poliglota.controller;

import com.poliglota.DTO.InvoiceDTO;
import com.poliglota.model.mysql.Invoice;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(  Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByUser(  Long userId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByUser(userId));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByUserByStatus(Long userId, String status) {
        return ResponseEntity.ok(invoiceService.getInvoicesByUserByStatus(userId, status));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByStatus(  String status) {
        return ResponseEntity.ok(invoiceService.getInvoicesByStatus(status));
    }

    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(
              Long userId,
              ProcessRequest process) {
        return ResponseEntity.ok(invoiceService.createInvoice(userId, process));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceDTO> pagarFactura(Long id, double monto, String metodoPago) {
        return ResponseEntity.ok(invoiceService.pagarFactura(id, monto, metodoPago));
    }
}
