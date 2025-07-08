package com.mdmc.posofmyheart.application.dtos;

import lombok.Builder;

@Builder
public record ProductImageResponse(
        String name,
        byte[] image,
        String base64
) {
}
