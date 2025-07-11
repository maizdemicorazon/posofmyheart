package com.mdmc.posofmyheart.application.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;

@Mapper
public interface ProductVariantMapper {
    ProductVariantMapper INSTANCE = Mappers.getMapper(ProductVariantMapper.class);

    List<ProductVariant> toDomainVariants(List<ProductVariantEntity> entities);

    @Mapping(target = "price", source = "actualSellPrice")
    ProductVariant toProductVariant(ProductVariantEntity entity);
}