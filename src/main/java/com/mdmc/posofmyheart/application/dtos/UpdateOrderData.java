package com.mdmc.posofmyheart.application.dtos;

/**
 * Record para transportar datos de actualización de orden.
 * Usado por UpdateOrderStrategy para encapsular parámetros.
 */
public record UpdateOrderData(
        Long idOrder,
        OrderUpdateRequest updateRequest
) {
    public UpdateOrderData {
        // Validaciones básicas
        if (idOrder == null || idOrder <= 0) {
            throw new IllegalArgumentException("idOrder no puede ser null o menor o igual a cero");
        }
        if (updateRequest == null) {
            throw new IllegalArgumentException("updateRequest no puede ser null");
        }
    }
}