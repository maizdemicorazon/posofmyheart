package com.mdmc.posofmyheart.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Sabor no encontrado")
public class FlavorNotFoundException extends RuntimeException {
}