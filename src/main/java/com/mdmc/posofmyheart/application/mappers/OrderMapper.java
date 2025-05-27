package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderExtrasDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "idOrder", source = "idOrder")
    @Mapping(target = "bill", source = "totalAmount")
    @Mapping(target = "paymentMethod", source = "paymentMethod.idPayment")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "items", source = "orderDetails")
    OrderResponse toResponse(OrderEntity entity);

    @Mapping(target = "idProduct", source = "product.idProduct")
    @Mapping(target = "idVariant", source = "variant.idVariant")
    @Mapping(target = "extras", source = "extraDetails")
    @Mapping(target = "sauces", source = "sauceDetails")
    OrderResponse.OrderItemResponse toItemResponse(OrderDetailEntity entity);

    @Mapping(target = "idExtra", source = "productExtra.idExtra")
    @Mapping(target = "quantity", source = "quantity")
    OrderResponse.OrderExtrasResponse toExtrasResponse(OrderExtrasDetailEntity entity);


    @Mapping(target = "idSauce", source = "sauce.idSauce")
    OrderResponse.OrderDetailSauceResponse toDetailSauceResponse(OrderDetailSauceEntity entity);

}
