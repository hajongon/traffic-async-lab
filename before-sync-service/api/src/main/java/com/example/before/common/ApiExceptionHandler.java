package com.example.before.common;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(Exception e) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "error", e.getClass().getSimpleName(),
                "message", e.getMessage() == null ? "request failed" : e.getMessage()
        );
    }
}
