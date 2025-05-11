package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    List<ProductVariant> toDomainVariants(List<ProductVariantEntity> variants);

    @Mapping(target = "selected", source = "isDefault")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "name", source = "product.name")
    ProductVariant toDomainVariant(ProductVariantEntity variant);

}
