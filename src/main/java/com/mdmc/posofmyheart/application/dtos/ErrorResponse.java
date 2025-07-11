package com.mdmc.posofmyheart.application.dtos;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message,
        Integer status,
        LocalDateTime timestamp,
        String debug,
        String type
) {
}