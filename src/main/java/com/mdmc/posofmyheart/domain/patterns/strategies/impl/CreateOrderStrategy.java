package com.mdmc.posofmyheart.domain.patterns.strategies.impl;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.application.services.OrderPersistenceService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import com.mdmc.posofmyheart.domain.patterns.factory.OrderFactory;
import com.mdmc.posofmyheart.domain.patterns.strategies.OrderOperationStrategy;
import com.mdmc.posofmyheart.domain.patterns.validator.OrderValidator;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateOrderStrategy implements OrderOperationStrategy<OrderRequest, CreateOrderResponse> {
    private final OrderFactory orderFactory;
    private final OrderValidator orderValidator;
    private final OrderPersistenceService persistenceService;
    private final OrderCalculationService calculationService;

    @Override
    public CreateOrderResponse execute(OrderRequest orderRequest) {
        // Validar la orden
        orderValidator.validateOrderRequest(orderRequest);

        // Crear la orden usando el factory
        OrderEntity order = orderFactory.createOrder(orderRequest);

        // Calcular el total
        order.setTotalAmount(calculationService.calculateOrderTotal(order));

        // Persistir la orden
        return persistenceService.saveOrder(order);
    }
}
