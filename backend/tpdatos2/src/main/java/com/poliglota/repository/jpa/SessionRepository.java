package com.poliglota.repository.jpa;

import com.poliglota.model.mysql.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
     List<Session> findByUser_UserIdAndStatus(Long userId, String status);
}
