package com.mdmc.posofmyheart.application.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public record OrderGroupedResponse(
        List<OrderResponse> receivedOrders,
        List<OrderResponse> attendingOrders,
        List<OrderResponse> completedOrders
) {
    public OrderGroupedResponse {
        receivedOrders = receivedOrders != null ? receivedOrders : List.of();
        attendingOrders = attendingOrders != null ? attendingOrders : List.of();
        completedOrders = completedOrders != null ? completedOrders : List.of();
    }
}
