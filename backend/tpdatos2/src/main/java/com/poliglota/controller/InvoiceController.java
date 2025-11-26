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

    //  Obtener todas las facturas
    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    //  Obtener factura por ID
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(  Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    //  Obtener facturas por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByUser(  Long userId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByUser(userId));
    }

    //  Obtener facturas por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByStatus(  String status) {
        return ResponseEntity.ok(invoiceService.getInvoicesByStatus(status));
    }

    //  Crear una nueva factura
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(
              Long userId,
              List<ProcessRequest> processes) {
        return ResponseEntity.ok(invoiceService.createInvoice(userId, processes));
    }

    //  Cambiar estado de una factura
    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceDTO> updateStatus(
              Long id,
               String newStatus) {
        return ResponseEntity.ok(invoiceService.updateStatus(id, newStatus));
    }
}
