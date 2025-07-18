package com.mdmc.posofmyheart.domain.patterns.strategies;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.application.services.OrderPersistenceService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.domain.patterns.factory.OrderFactory;
import com.mdmc.posofmyheart.domain.patterns.validator.OrderValidator;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;

@Component
@AllArgsConstructor
public class CreateOrderStrategy implements OrderOperationStrategy<OrderRequest, CreateOrderResponseDto> {
    private final OrderFactory orderFactory;
    private final OrderValidator orderValidator;
    private final OrderPersistenceService persistenceService;
    private final OrderCalculationService calculationService;

    @Override
    public CreateOrderResponseDto execute(OrderRequest orderRequest) {
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
