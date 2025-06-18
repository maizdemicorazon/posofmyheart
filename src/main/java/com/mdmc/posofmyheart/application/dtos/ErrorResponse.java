package com.mdmc.posofmyheart.application.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String message,
        Integer status,
        LocalDateTime timestamp,
        String debug,
        String type
) {
}