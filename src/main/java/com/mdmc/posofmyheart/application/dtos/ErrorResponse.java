package com.mdmc.posofmyheart.application.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String message,
        Integer status,
        String error,
        LocalDateTime timestamp,
        String debug,
        String type

) {
    public ErrorResponse(String message, Integer status, String path) {
        this(message, status, path, LocalDateTime.now(), null, null);
    }
}