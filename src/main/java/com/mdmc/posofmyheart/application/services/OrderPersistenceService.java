package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;

public interface OrderPersistenceService {
    CreateOrderResponseDto saveOrder(OrderEntity order);
}
