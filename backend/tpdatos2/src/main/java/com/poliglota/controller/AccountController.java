package com.poliglota.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poliglota.service.AccountService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	
	
}
