package com.project.service;

import com.project.model.mongodb.Payment;
import com.project.repository.mongodb.PaymentRepository;
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
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
