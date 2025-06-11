package com.mdmc.posofmyheart.application.dtos;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mdmc.posofmyheart.util.DateTimeUtils.randomEveningDateTime;

@Builder
public record OrderRestore(
        LocalDate restoreDate,
        List<OrderRequest> orderRequests
) {
    public static LocalDateTime addOrderTime(LocalDate date) {
        return randomEveningDateTime(date);
    }
}
