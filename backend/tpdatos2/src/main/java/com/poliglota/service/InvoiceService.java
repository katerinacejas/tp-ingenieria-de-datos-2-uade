package com.poliglota.service;

import com.poliglota.model.mysql.Invoice;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.model.mysql.User;
import com.poliglota.repository.InvoiceRepository;
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

    //  Obtener todas las facturas
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    //  Obtener factura por ID
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + id));
    }

    //  Obtener facturas por usuario
    public List<Invoice> getInvoicesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
        return invoiceRepository.findByUser(user);
    }

    //  Obtener facturas por estado
    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status);
    }

    //  Crear factura nueva
    public Invoice createInvoice(Long userId, List<ProcessRequest> processes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setBilledProcesses(processes);
        invoice.setStatus("pendiente");

        return invoiceRepository.save(invoice);
    }

    //  Cambiar estado de factura
    public Invoice updateStatus(Long id, String newStatus) {
        Invoice invoice = getInvoiceById(id);
        invoice.setStatus(newStatus);
        return invoiceRepository.save(invoice);
    }

    //  Eliminar factura
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}
