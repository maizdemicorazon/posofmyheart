package com.mdmc.posofmyheart.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Category(
        Long idCategory,
        @NotBlank(message = "El nombre de categoría es obligatorio")
        @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
        String name,
        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String description
) {
    public Category {
        if (name != null) {
            name = name.trim();
        }
        if (description != null) {
            description = description.trim();
        }
    }
}
