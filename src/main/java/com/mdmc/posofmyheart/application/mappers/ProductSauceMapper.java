package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CatalogImageMapper.class})
public interface ProductSauceMapper {
    ProductSauceMapper INSTANCE = Mappers.getMapper(ProductSauceMapper.class);

    @Mapping(target = "idImage", source = "image.idImage")
    ProductSauce toProductSauce(ProductSauceEntity sauceEntity);
}
