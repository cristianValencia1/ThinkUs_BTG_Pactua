package com.btg.funds.infrastructure.exception;

import com.btg.funds.application.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException exception) {
        HttpStatus status = switch (exception.getCode()) {
            case "CLIENT_NOT_FOUND", "FUND_NOT_FOUND", "SUBSCRIPTION_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "SUBSCRIPTION_ALREADY_EXISTS" -> HttpStatus.CONFLICT;
            case "INSUFFICIENT_BALANCE" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity.status(status).body(ApiError.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Solicitud invalida");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.builder()
                .code("VALIDATION_ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(HttpMessageNotReadableException exception) {
        String message = "JSON invalido o valores no permitidos. Verifica el formato de la solicitud";

        Throwable cause = exception.getMostSpecificCause();
        if (cause != null && cause.getMessage() != null && cause.getMessage().contains("NotificationPreference")) {
            message = "notificationPreference debe ser EMAIL o SMS";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.builder()
                .code("INVALID_REQUEST")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiError.builder()
                .code("INTERNAL_ERROR")
                .message("Ocurrio un error inesperado")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
