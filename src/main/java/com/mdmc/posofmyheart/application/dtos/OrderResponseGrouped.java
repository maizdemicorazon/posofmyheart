package com.mdmc.posofmyheart.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public record OrderResponseGrouped(
        List<OrderResponse> receivedOrders,
        List<OrderResponse> attendingOrders,
        List<OrderResponse> completedOrders
) {
    public OrderResponseGrouped {
        receivedOrders = receivedOrders != null ? receivedOrders : List.of();
        attendingOrders = attendingOrders != null ? attendingOrders : List.of();
        completedOrders = completedOrders != null ? completedOrders : List.of();
    }
}
