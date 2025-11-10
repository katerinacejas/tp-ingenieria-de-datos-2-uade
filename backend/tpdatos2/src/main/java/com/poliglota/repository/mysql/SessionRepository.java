package com.poliglota.repository.mysql;

import com.poliglota.model.mysql.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByUserIdAndStatus(String userId, String status);
}
