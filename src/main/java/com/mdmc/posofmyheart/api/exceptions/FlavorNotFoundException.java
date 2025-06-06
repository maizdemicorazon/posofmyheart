package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FlavorNotFoundException extends RuntimeException {
    public FlavorNotFoundException(Long idFlavor) {
        super(String.format("Sabor con ID %d no encontrado", idFlavor));
    }

    public FlavorNotFoundException(String productName) {
        super(String.format("Sabor '%s' no encontrado", productName));
    }
}