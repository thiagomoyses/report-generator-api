package com.tsm.report_generator_api.auth.dto;

public record LoginRequestDto(
        String username,
        String password
) {
}
