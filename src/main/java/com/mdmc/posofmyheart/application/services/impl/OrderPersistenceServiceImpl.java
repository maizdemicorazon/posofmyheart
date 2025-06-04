package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.services.OrderPersistenceService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPersistenceServiceImpl implements OrderPersistenceService {
    private final OrderRepository orderRepository;

    public CreateOrderResponseDto saveOrder(OrderEntity order) {
        return new CreateOrderResponseDto(
                orderRepository.save(order).getIdOrder()
        );
    }

    public List<CreateOrderResponseDto> saveOrders(List<OrderEntity> orders) {
        return orders.stream()
                .map(this::saveOrder)
                .toList();
    }
}
