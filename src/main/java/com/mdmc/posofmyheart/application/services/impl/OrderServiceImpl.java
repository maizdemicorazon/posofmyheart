package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.OrderNotFoundException;
import com.mdmc.posofmyheart.application.dtos.*;
import com.mdmc.posofmyheart.application.mappers.OrderResponseMapper;
import com.mdmc.posofmyheart.application.mappers.OrderRestoreMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.domain.patterns.strategies.CreateOrderStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.CreateOrdersStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.UpdateOrderStrategy;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mdmc.posofmyheart.application.dtos.OrderRestore.addOrderTime;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CreateOrderStrategy createOrderStrategy;
    private final CreateOrdersStrategy createOrdersStrategy;
    private final UpdateOrderStrategy updateOrderStrategy;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderRestore findOrdersToBackup() {
        return OrderRestoreMapper.INSTANCE.toBackup(findAllOrders());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> listOrdersByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return orderRepository.findByOrderDate(startOfDay, endOfDay)
                .stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CreateOrderResponseDto createOrder(OrderRequest request) {
        return createOrderStrategy.execute(request);
    }

    @Override
    @Transactional
    public List<OrderRequest> restoreBackup(OrderRestore restore) {
        return restore.orderRequests().stream()
                .map(or -> buildOrderRequest(or, restore.restoreDate()))
                .map(createOrdersStrategy::execute)
                .toList();
    }


    private OrderRequest buildOrderRequest(OrderRequest request, LocalDate restoreDate) {
        return new OrderRequest(
                request.idPaymentMethod(),
                request.clientName(),
                request.comment(),
                addOrderTime(restoreDate),
                request.items()
        );
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        UpdateOrderData updateData = new UpdateOrderData(idOrder, updateRequest);
        return updateOrderStrategy.execute(updateData);
    }

    @Override
    @Transactional
    public void deleteOrder(Long idOrder) {
        OrderEntity order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        orderRepository.delete(order);
    }

}
