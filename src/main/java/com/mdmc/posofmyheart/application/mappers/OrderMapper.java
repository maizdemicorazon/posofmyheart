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

    @Mapping(target = "idOrder", source = "idOrder")
    @Mapping(target = "bill", source = "totalAmount")
    OrderResponse toResponse(OrderEntity entity);

}
