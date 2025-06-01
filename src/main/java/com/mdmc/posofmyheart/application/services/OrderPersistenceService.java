package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;

public interface OrderPersistenceService {
    CreateOrderResponse saveOrder(OrderEntity order);
}
