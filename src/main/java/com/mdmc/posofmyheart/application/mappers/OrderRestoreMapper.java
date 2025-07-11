package com.mdmc.posofmyheart.application.mappers;

import java.time.LocalDate;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderRestore;

@Mapper
public interface OrderRestoreMapper {

    OrderRestoreMapper INSTANCE = Mappers.getMapper(OrderRestoreMapper.class);

    default OrderRestore toBackup(List<OrderResponse> orderResponses) {
        return OrderRestore.builder()
                .restoreDate(LocalDate.now())
                .orderRequests(OrderRequestMapper.INSTANCE.toOrderRequests(orderResponses))
                .build();
    }


}
