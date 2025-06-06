package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(Long idVariant) {
        super(String.format("Variante con ID %d no encontrado", idVariant));
    }

    public VariantNotFoundException(String productName) {
        super(String.format("Variante '%s' no encontrado", productName));
    }
}