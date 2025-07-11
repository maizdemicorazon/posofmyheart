package com.mdmc.posofmyheart.domain.patterns.chain;

import org.springframework.stereotype.Component;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;

@Component
public class OrderItemProcessorChain {
    private final OrderItemProcessor firstProcessor;

    public OrderItemProcessorChain(OrderItemProcessor firstProcessor) {
        this.firstProcessor = firstProcessor;
    }

    public void process(OrderDetailEntity detail, OrderItemRequest item) {
        firstProcessor.process(detail, item);
    }
}