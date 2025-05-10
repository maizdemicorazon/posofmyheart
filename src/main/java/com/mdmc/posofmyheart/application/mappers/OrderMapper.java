package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "paymentMethod", source = "paymentMethod.name")
    @Mapping(target = "items", source = "details")
    OrderResponse toResponse(OrderEntity entity);

    default List<OrderResponse.OrderItemResponse> mapDetailsToItems(List<OrderDetailEntity> details) {
        return details.stream()
                .map(this::toItemResponse)
                .toList();
    }

    private OrderResponse.OrderItemResponse toItemResponse(OrderDetailEntity detail) {
        return new OrderResponse.OrderItemResponse(
                detail.getProduct().getId(),
                detail.getProduct().getName(),
                detail.getQuantity(),
                detail.getUnitPrice()
        );
    }
}