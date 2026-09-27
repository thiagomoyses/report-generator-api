package com.tsm.report_generator_api.auth.service;

import com.tsm.report_generator_api.auth.dto.AuthResponseDto;
import com.tsm.report_generator_api.auth.dto.LoginRequestDto;
import com.tsm.report_generator_api.auth.dto.RegisterRequest;
import com.tsm.report_generator_api.auth.dto.RegisterResponseDto;
import com.tsm.report_generator_api.auth.entity.UserEntity;
import com.tsm.report_generator_api.auth.repository.UserRepository;
import com.tsm.report_generator_api.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponseDto registration(RegisterRequest requestDto) {
        if (userRepository.existsByUsername(requestDto.username())) {
            throw new RuntimeException("Username already in use.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.password());

        UserEntity user = new UserEntity(
                requestDto.name(),
                requestDto.username(),
                encodedPassword
        );

        return new RegisterResponseDto(
                "User registered successfully",
                user.getUsername());
    }

    public AuthResponseDto login(LoginRequestDto request) {

        UserEntity user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getUsername()
                );

        return new AuthResponseDto(
                token,
                user.getUsername()
        );
    }
}
