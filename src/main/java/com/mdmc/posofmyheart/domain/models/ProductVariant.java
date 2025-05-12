package com.mdmc.posofmyheart.domain.models;

import java.math.BigDecimal;

public record ProductVariant(
        String size,
        BigDecimal price
) {
}
