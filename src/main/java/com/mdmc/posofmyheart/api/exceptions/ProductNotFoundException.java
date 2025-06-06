package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super(String.format("Producto con ID %d no encontrado", productId));
    }

    public ProductNotFoundException(String productName) {
        super(String.format("Producto '%s' no encontrado", productName));
    }
}