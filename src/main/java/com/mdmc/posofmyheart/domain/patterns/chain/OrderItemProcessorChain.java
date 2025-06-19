package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.PreloadedEntities;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemProcessorChain {
    private final OrderItemProcessor firstProcessor;

    public OrderItemProcessorChain(OrderItemProcessor firstProcessor) {
        this.firstProcessor = firstProcessor;
    }

    public void process(OrderDetailEntity detail, OrderItemRequest item, PreloadedEntities preloaded) {
        firstProcessor.process(detail, item, preloaded);
    }
}