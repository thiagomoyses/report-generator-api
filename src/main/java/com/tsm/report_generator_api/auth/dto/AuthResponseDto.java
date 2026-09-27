package com.tsm.report_generator_api.auth.dto;

public record AuthResponseDto(
        String token,
        String username
) {
}
