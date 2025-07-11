package com.mdmc.posofmyheart.application.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderFlavorDetailEntity;

@Mapper(uses = {CatalogImageMapper.class})
public interface OrderResponseMapper {

    OrderResponseMapper INSTANCE = Mappers.getMapper(OrderResponseMapper.class);

    @Mapping(target = "idOrder", source = "idOrder")
    @Mapping(target = "bill", source = "totalAmount")
    @Mapping(target = "idPaymentMethod", source = "paymentMethod.idPaymentMethod")
    @Mapping(target = "paymentName", source = "paymentMethod.name")
    @Mapping(target = "items", source = "orderDetails")
    OrderResponse toResponse(OrderEntity entity);

    @Mapping(target = "idProduct", source = "product.idProduct")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", source = "product.image.idImage", ignore = true)
    @Mapping(target = "productPrice", source = "sellPrice")
    @Mapping(target = "idVariant", source = "variant.idVariant")
    @Mapping(target = "variantName", source = "variant.size")
    @Mapping(target = "extras", source = "extraDetails")
    @Mapping(target = "sauces", source = "sauceDetails")
    @Mapping(target = "flavor", source = "flavorDetails", qualifiedByName = "toOneFlavor")
    OrderResponse.OrderItemResponse toItemResponse(OrderDetailEntity entity);

    @Mapping(target = "idExtra", source = "productExtra.idExtra")
    @Mapping(target = "name", source = "productExtra.name")
    @Mapping(target = "actualPrice", source = "productExtra.actualPrice")
    @Mapping(target = "quantity", source = "quantity")
    OrderResponse.OrderExtrasResponse toExtrasResponse(OrderExtraDetailEntity entity);

    @Mapping(target = "idSauce", source = "productSauce.idSauce")
    @Mapping(target = "name", source = "productSauce.name")
    OrderResponse.OrderDetailSauceResponse toDetailSauceResponse(OrderDetailSauceEntity entity);

    @Named("toOneFlavor")
    default OrderResponse.OrderFlavorDetailResponse toOneFlavor(Set<OrderFlavorDetailEntity> flavorDetails) {
        return flavorDetails.stream()
                .findFirst()
                .map(this::toFlavorDetailResponse)
                .orElse(null);
    }

    @Mapping(target = "idFlavor", source = "flavor.idFlavor")
    @Mapping(target = "name", source = "flavor.name")
    @Mapping(target = "idOrderDetail", source = "orderDetail.idOrderDetail")
    OrderResponse.OrderFlavorDetailResponse toFlavorDetailResponse(OrderFlavorDetailEntity entity);
}