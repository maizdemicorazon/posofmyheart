package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;

@Mapper
public interface ProductExtrasEntityMapper {
    ProductExtrasEntityMapper INSTANCE = Mappers.getMapper(ProductExtrasEntityMapper.class);

    default List<ProductExtraEntity> toExtrasOrderedById(List<ProductExtraEntity> extras) {
        return extras.stream()
                .sorted(
                        Comparator.comparing(
                                ProductExtraEntity::getIdExtra
                        )
                )
                .toList();
    }
}
