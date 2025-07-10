package com.mdmc.posofmyheart.application.mappers;

import java.util.List;

import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CatalogImageMapper.class})
public interface ProductSauceMapper {
    ProductSauceMapper INSTANCE = Mappers.getMapper(ProductSauceMapper.class);

    @Mapping(target = "image", source = "image", qualifiedByName = "catalogImageToByteArray")
    ProductSauce toProductSauce(ProductSauceEntity sauceEntity);

    List<ProductSauce> toProductSauces(List<ProductSauceEntity> sauceEntities);
}