package com.mdmc.posofmyheart.api.exceptions;

import com.mdmc.posofmyheart.application.dtos.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String PETITION_ERROR_LOG = "Error en la petición {}: {}";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Constraint Violation");
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Type Mismatch");
        response.put("message", String.format("Invalid value for parameter '%s': %s", ex.getName(), ex.getValue()));

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unhandled exception: ", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", "An unexpected error occurred");

        // En desarrollo, incluir más detalles
        if (isDevEnvironment()) {
            response.put("debug", ex.getMessage());
            response.put("type", ex.getClass().getSimpleName());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private boolean isDevEnvironment() {
        String profile = System.getProperty("spring.profiles.active", "");
        return profile.contains("dev") || profile.contains("local");
    }

    // Manejadores específicos para cada excepción personalizada
    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMenuNotFound(MenuNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(OrderDetailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderDetailNotFound(OrderDetailNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(PayMethodNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePayMethodNotFound(PayMethodNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(ProductExtraNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductExtraNotFound(ProductExtraNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(SauceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSauceNotFound(SauceNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(VariantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVariantNotFound(VariantNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    // Manejador especial para ResourceNotFoundException (sin @ResponseStatus)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), ex.getClass().getSimpleName(), request.getDescription(false)));
    }

    // Método helper para respuestas 404 estandarizadas
    private ResponseEntity<ErrorResponse> buildNotFoundResponse(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponse(
                               ex.getMessage(),
                                ex.getClass().getSimpleName(),
                                request.getDescription(false)
                        )
                );
    }

    private static void logError(WebRequest request, String errorMessage) {
        log.error(PETITION_ERROR_LOG, request.getDescription(false), errorMessage);
    }
}