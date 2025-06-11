package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderResponseMapper {

    OrderResponseMapper INSTANCE = Mappers.getMapper(OrderResponseMapper.class);

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
    @Mapping(target = "flavor", source = "flavorDetails", qualifiedByName = "toOneFlavor")
    OrderResponse.OrderItemResponse toItemResponse(OrderDetailEntity entity);

    @Mapping(target = "idExtra", source = "productExtra.idExtra")
    @Mapping(target = "quantity", source = "quantity")
    OrderResponse.OrderExtrasResponse toExtrasResponse(OrderExtraDetailEntity entity);

    @Mapping(target = "idSauce", source = "productSauce.idSauce")
    @Mapping(target = "name", source = "productSauce.name")
    OrderResponse.OrderDetailSauceResponse toDetailSauceResponse(OrderDetailSauceEntity entity);

    @Mapping(target = "name", source = "flavor.name")
    @Mapping(target = "idFlavor", source = "flavor.idFlavor")
    @Mapping(target = "idOrderDetail", source = "orderDetail.idOrderDetail")
    OrderResponse.OrderFlavorDetailResponse toFlavorDetailResponse(OrderFlavorDetailEntity entity);

    @Named("toOneFlavor")
    default OrderResponse.OrderFlavorDetailResponse toOneFlavor(List<OrderFlavorDetailEntity> flavorDetails) {
        return flavorDetails.stream().findFirst().map(this::toFlavorDetailResponse).orElseThrow();
    }

}
