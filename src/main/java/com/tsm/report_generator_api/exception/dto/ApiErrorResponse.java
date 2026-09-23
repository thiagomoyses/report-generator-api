package com.tsm.report_generator_api.exception.dto;



public record ApiErrorResponse(
        String status,
        String message,
        String timestamp
) {
}
