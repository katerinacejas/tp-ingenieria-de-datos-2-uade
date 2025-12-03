package com.poliglota.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poliglota.DTO.SessionDTO;
import com.poliglota.service.SessionService;

@RestController
@RequestMapping("/api/session")
public class SessionController {
	@Autowired
	private SessionService sessionService;

	public List<SessionDTO> getAllSessions() {
		return sessionService.getAllSessions();
	}
}
