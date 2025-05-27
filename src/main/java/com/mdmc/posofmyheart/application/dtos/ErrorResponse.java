package com.mdmc.posofmyheart.application.dtos;

import java.time.LocalDateTime;

// Clase para respuestas de error estandarizadas
public record ErrorResponse(String message, LocalDateTime timestamp, String errorType) {
    public ErrorResponse(String message, String errorType) {
        this(message, LocalDateTime.now(), errorType);
    }
}