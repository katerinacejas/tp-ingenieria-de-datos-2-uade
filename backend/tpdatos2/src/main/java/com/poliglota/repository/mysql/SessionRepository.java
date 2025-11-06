package com.project.repository.mongodb;

import com.project.model.mongodb.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    List<Session> findByUserIdAndStatus(String userId, String status);
}
