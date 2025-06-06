package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long idOrder) {
        super(String.format("Orden con ID %d no encontrado", idOrder));
    }

    public OrderNotFoundException(String productName) {
        super(String.format("Orden '%s' no encontrado", productName));
    }
}