package com.mdmc.posofmyheart.domain.models;

import java.math.BigDecimal;

public record ProductExtra(
        Integer id,
        String name,
        BigDecimal price
) {
}
