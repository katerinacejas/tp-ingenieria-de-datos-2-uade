package com.poliglota.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- DTO de error ---
    public static class ErrorResponse {
        public final String error;
        public final String message;
        public final int status;
        public final String path;
        public final OffsetDateTime timestamp;

        public ErrorResponse(String error, String message, int status, String path) {
            this.error = error;
            this.message = message;
            this.status = status;
            this.path = path;
            this.timestamp = OffsetDateTime.now();
        }
    }

    // Exception de dominio
    public static class InvalidStateException extends RuntimeException {
        public InvalidStateException() {
            super();
        }
        public InvalidStateException(String message) {
            super(message);
        }
        public InvalidStateException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Util para armar la respuesta
    private ResponseEntity<ErrorResponse> build(HttpStatus status, String msg, String path) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.getReasonPhrase(), msg, status.value(), path));
    }

    // --- Handlers ESPECÍFICOS (uno por excepción, sin listas que se pisen) ---

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNotFound(UsuarioNotFoundException ex, jakarta.servlet.http.HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ProcessNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProcessNotFound(ProcessNotFoundException ex, jakarta.servlet.http.HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ProcessRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProcessRequestNotFound(ProcessRequestNotFoundException ex, jakarta.servlet.http.HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    // --- Otros casos de dominio (si los tenés) ---
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidStateException ex, jakarta.servlet.http.HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), req.getRequestURI());
    }

    // --- Fallbacks (ordenados de más específico a más general) ---
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, jakarta.servlet.http.HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, jakarta.servlet.http.HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, jakarta.servlet.http.HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI());
    }
}
