package com.tsm.report_generator_api.auth.dto;

public record RegisterRequest(
        String name,
        String username,
        String password
) {
}
