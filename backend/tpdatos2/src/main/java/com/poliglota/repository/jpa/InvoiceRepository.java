package com.poliglota.repository.jpa;

import com.poliglota.model.mysql.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poliglota.model.mysql.User;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
   //  Buscar facturas por usuario
    List<Invoice> findByUser(User user);

    //  Buscar facturas por estado
    List<Invoice> findByStatus(String status);
}
