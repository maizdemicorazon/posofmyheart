package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;

@Mapper
public interface ProductEntityMapper {
    ProductEntityMapper INSTANCE = Mappers.getMapper(ProductEntityMapper.class);

    default List<ProductEntity> toProductsOrderedById(List<ProductEntity> products) {
        return products.stream()
                .sorted(
                        Comparator.comparing(
                                ProductEntity::getIdProduct
                        )
                )
                .toList();
    }
}
