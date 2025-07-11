package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderRequestMapper {

    OrderRequestMapper INSTANCE = Mappers.getMapper(OrderRequestMapper.class);

    @Mapping(target = "idPaymentMethod", source = "idPaymentMethod")
    @Mapping(target = "items", source = "items")
    OrderRequest toOrderRequest(OrderResponse orderResponse);

    List<OrderRequest> toOrderRequests(List<OrderResponse> orderResponses);

    default Long toIdFlavor(OrderResponse.OrderFlavorDetailResponse detailResponse) {
        return detailResponse != null ? detailResponse.idFlavor() : null;
    }
}

