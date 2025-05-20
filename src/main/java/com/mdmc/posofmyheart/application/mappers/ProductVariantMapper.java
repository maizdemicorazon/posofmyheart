package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductVariantMapper {
    ProductVariantMapper INSTANCE = Mappers.getMapper(ProductVariantMapper.class);

    List<ProductVariant> toDomainVariants(List<ProductVariantEntity> entities);

    @Mapping(target = "price", source = "sellPrice")
    ProductVariant toDomain(ProductVariantEntity entity);
}