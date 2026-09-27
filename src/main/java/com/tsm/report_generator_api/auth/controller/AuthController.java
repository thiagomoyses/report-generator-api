package com.tsm.report_generator_api.auth.controller;

import com.tsm.report_generator_api.auth.dto.AuthResponseDto;
import com.tsm.report_generator_api.auth.dto.LoginRequestDto;
import com.tsm.report_generator_api.auth.dto.RegisterRequest;
import com.tsm.report_generator_api.auth.dto.RegisterResponseDto;
import com.tsm.report_generator_api.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(
            @RequestBody RegisterRequest request
    ) {

        RegisterResponseDto response = authService.registration(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody LoginRequestDto request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

}
