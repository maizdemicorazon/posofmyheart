package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PayMethodNotFoundException extends RuntimeException {
    public PayMethodNotFoundException(Long idPayment) {
        super(String.format("Forma de pago con ID %d no encontrado", idPayment));
    }

    public PayMethodNotFoundException(String productName) {
        super(String.format("Forma de pago '%s' no encontrado", productName));
    }
}