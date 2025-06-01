package com.mdmc.posofmyheart.domain.patterns.validator;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validateOrderRequest(OrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Order request cannot be null");
        }

        if (request.idPaymentMethod() == null) {
            throw new IllegalArgumentException("Payment method is required");
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("At least one item is required");
        }

        // Validar cada item
        request.items().forEach(this::validateOrderItem);
    }

    private void validateOrderItem(OrderItemRequest item) {
        if (item.idProduct() == null) {
            throw new IllegalArgumentException("Product ID is required for each item");
        }

        if (item.idVariant() == null) {
            throw new IllegalArgumentException("Variant ID is required for each item");
        }
    }

    public void validateOrderUpdate(OrderUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Update request cannot be null");
        }
    }
}
