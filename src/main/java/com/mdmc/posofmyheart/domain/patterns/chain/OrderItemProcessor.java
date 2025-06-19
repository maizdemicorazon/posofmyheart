package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.domain.patterns.facade.PreloadedEntities;

public abstract class OrderItemProcessor {
    protected OrderItemProcessor nextProcessor;

    public void setNext(OrderItemProcessor next) {
        this.nextProcessor = next;
    }

    public abstract void process(OrderDetailEntity detail, OrderItemRequest item, PreloadedEntities preloaded);

    protected void processNext(OrderDetailEntity detail, OrderItemRequest item, PreloadedEntities preloaded) {
        if (nextProcessor != null) {
            nextProcessor.process(detail, item, preloaded);
        }
    }
}