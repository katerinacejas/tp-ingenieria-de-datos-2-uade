package com.project.repository.mongodb;

import com.project.model.mongodb.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByInvoiceId(String invoiceId);
}
