package com.poliglota.service;

import com.poliglota.model.mysql.Invoice;
import com.poliglota.repository.InvoiceRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getInvoicesByUser(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public boolean deleteInvoice(String id) {
        try {
            Long invoiceId = Long.parseLong(id);
            if (invoiceRepository.existsById(invoiceId)) {
                invoiceRepository.deleteById(invoiceId);
                return true;
            }
        } catch (NumberFormatException e) {
            // Handle invalid id format if needed
        }
        return false;
    }
}
