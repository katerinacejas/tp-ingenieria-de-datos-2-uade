package com.poliglota.repository.jpa;

import com.poliglota.model.mysql.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poliglota.model.mysql.User;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUser(User user);

    List<Invoice> findByStatus(String status);

	List<Invoice> findByUserAndStatus(User user, String status);
}
