package com.poliglota.service;

import com.poliglota.model.mysql.Session;
import com.poliglota.repository.mysql.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<Session> getActiveSessionsByUser(String userId) {
        return sessionRepository.findByUserIdAndStatus(userId, "active");
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public boolean deleteSession(String id) {
        try {
            Long sessionId = Long.parseLong(id);
            if (sessionRepository.existsById(sessionId)) {
                sessionRepository.deleteById(sessionId);
                return true;
            }
        } catch (NumberFormatException e) {
            // Handle invalid id format if needed
        }
        return false;
    }
}
