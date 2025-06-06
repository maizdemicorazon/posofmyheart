package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderDetailNotFoundException extends RuntimeException {
    public OrderDetailNotFoundException(Long idOrderDetail) {
        super(String.format("Detalle de orden con ID %d no encontrado", idOrderDetail));
    }

    public OrderDetailNotFoundException(String productName) {
        super(String.format("Detalle de orden '%s' no encontrado", productName));
    }
}