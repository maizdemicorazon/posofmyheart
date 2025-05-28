package com.mdmc.posofmyheart.application.dtos;

import java.time.LocalDateTime;

// Clase para respuestas de error estandarizadas
public record ErrorResponse(
        String message,
        String errorCode,
        String path,
        LocalDateTime timestamp
) {

    public ErrorResponse(String message, String errorCode, String path) {
        this(message, errorCode, path, LocalDateTime.now());
    }
}