package com.supplychain.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(int status, String message,
                         Object errors) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors =
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400,
                        "Validation failed", errors));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuth(
            AuthException ex) {
        return ResponseEntity.status(401)
                .body(new ErrorResponse(401,
                        ex.getMessage(), null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
            AccessDeniedException ex) {
        return ResponseEntity.status(403)
                .body(new ErrorResponse(403,
                        "Access denied", null));
    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex) {
        log.error("Unhandled exception: ", ex);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(500,
                        "Internal error", null));
    }*/

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unhandled exception type: {}", ex.getClass().getName());
        log.error("Unhandled exception message: {}", ex.getMessage());
        log.error("Unhandled exception: ", ex);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(500, ex.getMessage(), null));
    }
}