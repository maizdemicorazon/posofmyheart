package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Salsa no encontrada")
public class SauceNotFoundException extends RuntimeException {
}