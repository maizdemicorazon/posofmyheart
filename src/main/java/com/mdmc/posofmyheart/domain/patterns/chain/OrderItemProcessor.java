package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;

public abstract class OrderItemProcessor {
    protected OrderItemProcessor nextProcessor;

    public void setNext(OrderItemProcessor next) {
        this.nextProcessor = next;
    }

    protected void processNext(OrderDetailEntity detail, OrderItemRequest item) {
        if (nextProcessor != null) {
            nextProcessor.process(detail, item);
        }
    }

    public abstract void process(OrderDetailEntity detail, OrderItemRequest item);
}