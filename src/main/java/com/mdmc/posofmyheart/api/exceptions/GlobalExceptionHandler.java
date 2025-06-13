package com.mdmc.posofmyheart.api.exceptions;

import com.mdmc.posofmyheart.application.dtos.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String PETITION_ERROR_LOG = "Error en la petición {}: {}";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";

    // Manejador para errores no controlados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Error no manejado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "Ocurrió un error inesperado",
                        ex.getClass().getSimpleName(),
                        request.getDescription(false)
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {

        logError(request, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                                ex.getMessage(),
                                VALIDATION_ERROR,
                                request.getDescription(false)
                        )
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {

        String parameterName = ex.getName();
        Class<?> requiredType = ex.getRequiredType();
        String requiredTypeName = requiredType != null ? requiredType.getSimpleName() : "tipo desconocido";

        String errorMessage = String.format(
                "El parámetro '%s' debe ser de tipo %s. Valor recibido: '%s'",
                parameterName,
                requiredTypeName,
                ex.getValue()
        );

        logError(request, errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                                errorMessage,
                                VALIDATION_ERROR,
                                request.getDescription(false)
                        )
                );
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