package com.mdmc.posofmyheart.api.exceptions;

import com.mdmc.posofmyheart.application.dtos.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.core.annotation.AnnotationUtils;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Método para extraer el 'reason' de @ResponseStatus o usar getMessage()
    private String getReasonFromException(RuntimeException ex) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        return (responseStatus != null) ? responseStatus.reason() : ex.getMessage();
    }

    // Manejador para errores no controlados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Error no manejado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "Ocurrió un error inesperado",
                        ex.getClass().getSimpleName()
                ));
    }

    // Manejadores específicos para cada excepción personalizada
    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMenuNotFound(MenuNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    @ExceptionHandler(OrderDetailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderDetailNotFound(OrderDetailNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    // [Repetir el mismo patrón para todas las demás excepciones...]
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    @ExceptionHandler(PayMethodNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePayMethodNotFound(PayMethodNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    @ExceptionHandler(ProductExtraNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductExtraNotFound(ProductExtraNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    @ExceptionHandler(SauceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSauceNotFound(SauceNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    @ExceptionHandler(VariantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVariantNotFound(VariantNotFoundException ex) {
        return buildNotFoundResponse(ex);
    }

    // Manejador especial para ResourceNotFoundException (sin @ResponseStatus)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), ex.getClass().getSimpleName()));
    }

    // Método helper para respuestas 404 estandarizadas
    private ResponseEntity<ErrorResponse> buildNotFoundResponse(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(getReasonFromException(ex), ex.getClass().getSimpleName()));
    }
}