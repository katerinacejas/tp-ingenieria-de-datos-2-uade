package com.poliglota.service;

import com.poliglota.DTO.SessionDTO;
import com.poliglota.model.mysql.Session;
import com.poliglota.repository.SessionRepository;
import com.poliglota.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.poliglota.model.mysql.User;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
	private final UserRepository userRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<Session> getActiveSessionsByUser(String userId) {
        return sessionRepository.findByUserIdAndStatus(userId, "active");
    }

    public SessionDTO createSession(SessionDTO sessionDTO) {
		User user = userRepository.findById(Long.parseLong(sessionDTO.getUserId())).orElse(null);
		return toDto(sessionRepository.save(toEntity(sessionDTO, user)));
    }

	public void closeSession(SessionDTO sessionDTO) {
		Session session = sessionRepository.findByUserIdAndStatus(sessionDTO.getUserId(), "activa").get(0);
		session.setStatus("inactiva");
		session.setEndTime(LocalDateTime.now());
		sessionRepository.save(session);
	}

	private Session toEntity(SessionDTO dto, User user) {
		Session session = new Session();
		session.setUser(user);
		session.setRol(user.getRolEntity());
		session.setStartTime(dto.getStartTime());
		session.setEndTime(dto.getEndTime());
		session.setStatus(dto.getStatus());
		return session;
	}

	private SessionDTO toDto(Session session) {
		SessionDTO dto = new SessionDTO();
		dto.setSessionId(session.getSessionId().toString());
		dto.setUserId(session.getUser().getUserId().toString());
		dto.setRolId(session.getRol().getCode().toString());
		dto.setStartTime(session.getStartTime());
		dto.setEndTime(session.getEndTime());
		dto.setStatus(session.getStatus());
		return dto;
	}
}
