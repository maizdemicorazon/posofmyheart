package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;

@Mapper
public interface ProductSauceEntityMapper {
    ProductSauceEntityMapper INSTANCE = Mappers.getMapper(ProductSauceEntityMapper.class);

    default List<ProductSauceEntity> toExtrasOrderedById(List<ProductSauceEntity> sauces) {
        return sauces.stream()
                .sorted(
                        Comparator.comparing(
                                ProductSauceEntity::getIdSauce
                        )
                )
                .toList();
    }
}