package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderCalculationServiceImpl implements OrderCalculationService {

    public BigDecimal calculateOrderTotal(OrderEntity order) {
        return order.getOrderDetails().stream()
                .map(this::calculateExtrasTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateExtrasTotal(OrderDetailEntity detail) {
        return detail.getExtraDetails().stream()
                .map(extra -> extra.getSellPrice()
                        .multiply(BigDecimal.valueOf(extra.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
