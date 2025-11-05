package com.project.service;

import com.project.model.mongodb.Session;
import com.project.repository.mongodb.SessionRepository;
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
        if (sessionRepository.existsById(id)) {
            sessionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
