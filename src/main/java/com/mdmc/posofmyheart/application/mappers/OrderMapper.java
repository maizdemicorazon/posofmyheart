package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderId", source = "idOrder")
    @Mapping(target = "paymentMethod", source = "paymentMethod.name")
    @Mapping(target = "items", source = "orderDetails")
    OrderResponse toResponse(OrderEntity entity);

    default List<OrderResponse.OrderItemResponse> toDomainItems(List<OrderDetailEntity> details) {
        return details.stream()
                .map(this::toItemResponse)
                .toList();
    }

    private OrderResponse.OrderItemResponse toItemResponse(OrderDetailEntity detail) {
        return new OrderResponse.OrderItemResponse(
                detail.getProduct().getIdProduct(),
                detail.getProduct().getName(),
                detail.getUnitPrice()
        );
    }
}