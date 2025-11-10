package com.poliglota.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.poliglota.service.AuthenticationService;
import com.poliglota.DTO.request.LoginRequestDTO;
import com.poliglota.DTO.request.RegistroRequestDTO;
import com.poliglota.DTO.response.JwtResponseDTO;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public JwtResponseDTO register(@RequestBody RegistroRequestDTO request) {
        return authenticationService.register(request);
    }
}
