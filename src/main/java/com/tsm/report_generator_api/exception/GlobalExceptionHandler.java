package com.tsm.report_generator_api.exception;

import com.tsm.report_generator_api.exception.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, RuntimeException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                "KO",
                ex.getMessage(),
                LocalDateTime.now().format(FORMATTER)
        );

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
