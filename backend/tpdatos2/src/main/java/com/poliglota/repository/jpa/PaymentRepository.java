package com.poliglota.repository.jpa;

import com.poliglota.model.mysql.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poliglota.model.mysql.Invoice;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByInvoice(Invoice invoice);

    List<Payment> findByPaymentMethod(String paymentMethod);

    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);
    
}
