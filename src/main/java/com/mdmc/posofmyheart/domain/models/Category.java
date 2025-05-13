package com.mdmc.posofmyheart.domain.models;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductCategoryEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Category(
        Integer idCategory,  // Null para categorías nuevas

        @NotBlank(message = "El nombre de categoría es obligatorio")
        @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
        String name,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String description
) {
    // Constructor compacto con validación adicional
    public Category {
        if (name != null) name = name.trim();
        if (description != null) description = description.trim();
    }

    // Método de conveniencia para crear desde Entity //@TODO Map struct
    public static Category fromEntity(ProductCategoryEntity entity) {
        return new Category(
                entity.getIdCategory(),
                entity.getName(),
                entity.getDescription()
        );
    }
}