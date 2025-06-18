package com.mdmc.posofmyheart.api.exceptions;

import com.mdmc.posofmyheart.application.dtos.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return buildBadRequestResponse(ex,
                String.format("Constraint Violations '%s': %s", ex.getConstraintViolations(), ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildBadRequestResponse(ex,
                String.format("Invalid value for parameter '%s': %s", ex.getName(), ex.getValue())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unhandled exception: ", ex);

        return buildHttpResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled exception");
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataAccessApiUsageException(
            InvalidDataAccessApiUsageException ex,
            WebRequest request
    ) {
        return buildHttpResponse(ex, request, HttpStatus.BAD_REQUEST, ex.getMessage());
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

    @ExceptionHandler(ProductSauceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSauceNotFound(ProductSauceNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    @ExceptionHandler(VariantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVariantNotFound(VariantNotFoundException ex, WebRequest request) {
        return buildNotFoundResponse(ex, request);
    }

    // Función helper para respuestas 404 estandarizadas
    private ResponseEntity<ErrorResponse> buildNotFoundResponse(
            Exception ex,
            WebRequest request
    ) {
        return buildHttpResponse(ex, request, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Función helper para respuestas status custom
    private ResponseEntity<ErrorResponse> buildHttpResponse(
            Exception ex,
            WebRequest request,
            HttpStatus status,
            String message
    ) {
        return ResponseEntity.status(status)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .message(message)
                                .status(status.value())
                                .debug(request.getDescription(false))
                                .type(ex.getClass().getSimpleName()).build()
                );
    }

    // Función helper para respuestas status custom
    private ResponseEntity<ErrorResponse> buildBadRequestResponse(
            Exception ex,
            String message
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .message(message)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .type(ex.getClass().getSimpleName()).build()
                );
    }
}