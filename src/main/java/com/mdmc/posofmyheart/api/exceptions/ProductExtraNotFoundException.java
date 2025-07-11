package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductExtraNotFoundException extends RuntimeException {
    public ProductExtraNotFoundException(Long idProductExtra) {
        super(String.format("Producto extra con ID %d no encontrado", idProductExtra));
    }

    public ProductExtraNotFoundException(String productName) {
        super(String.format("Producto extra '%s' no encontrado", productName));
    }
}