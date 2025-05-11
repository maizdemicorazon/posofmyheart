package com.mdmc.posofmyheart.domain.models;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductPriceEntity;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Price(
        Integer idPrice,
        BigDecimal sellPrice,
        BigDecimal costPrice
) {

    public static Optional<Price> to(List<ProductPriceEntity> entityPrices) {
        return entityPrices.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(ProductPriceEntity::getEffectiveDate))
                .map(Price::toRecord);
    }

    private static Price toRecord(ProductPriceEntity entity) {
        return new Price(
                entity.getIdPrice(),
                entity.getSellPrice(),
                entity.getCostPrice()
        );
    }

}
