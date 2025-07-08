package com.mdmc.posofmyheart.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

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
