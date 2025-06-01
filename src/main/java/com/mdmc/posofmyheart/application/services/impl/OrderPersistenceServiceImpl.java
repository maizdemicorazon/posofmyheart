package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.services.OrderPersistenceService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPersistenceServiceImpl implements OrderPersistenceService {
    private final OrderRepository orderRepository;

    public CreateOrderResponse saveOrder(OrderEntity order) {
        return new CreateOrderResponse(
                orderRepository.save(order).getIdOrder()
        );
    }

    public List<CreateOrderResponse> saveOrders(List<OrderEntity> orders) {
        return orders.stream()
                .map(this::saveOrder)
                .toList();
    }
}
