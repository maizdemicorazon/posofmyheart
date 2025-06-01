package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;

import java.math.BigDecimal;

public interface OrderCalculationService {
    BigDecimal calculateOrderTotal(OrderEntity order);
}
