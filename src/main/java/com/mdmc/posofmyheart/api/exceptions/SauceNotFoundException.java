package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SauceNotFoundException extends RuntimeException {
    public SauceNotFoundException(Long idSauce) {
        super(String.format("Salsa con ID %d no encontrado", idSauce));
    }

    public SauceNotFoundException(String productName) {
        super(String.format("Salsa '%s' no encontrado", productName));
    }
}