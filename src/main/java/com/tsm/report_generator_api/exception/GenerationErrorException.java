package com.tsm.report_generator_api.exception;

public class GenerationErrorException extends RuntimeException {
    public GenerationErrorException(String message) {
        super(message);
    }
}
