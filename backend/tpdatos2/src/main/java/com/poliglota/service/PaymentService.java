package com.poliglota.service;

import com.poliglota.model.mysql.Payment;
import com.poliglota.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByInvoice(String invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public boolean deletePayment(String id) {
        try {
            Long paymentId = Long.parseLong(id);
            if (paymentRepository.existsById(paymentId)) {
                paymentRepository.deleteById(paymentId);
                return true;
            }
        } catch (NumberFormatException e) {
            // Handle invalid id format if needed
        }
        return false;
    }
}
