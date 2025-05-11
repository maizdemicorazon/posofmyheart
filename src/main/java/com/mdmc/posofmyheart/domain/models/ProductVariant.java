package com.mdmc.posofmyheart.domain.models;

import java.util.List;

public record ProductVariant(
        String name,
        String size,
        boolean selected,
        List<ProductPrice> prices
) {
}
