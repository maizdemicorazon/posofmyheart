package com.mdmc.posofmyheart.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;

@Mapper(uses = {CatalogImageMapper.class})
public interface ProductExtraMapper {

    ProductExtraMapper INSTANCE = Mappers.getMapper(ProductExtraMapper.class);

    @Mapping(target = "price", source = "actualPrice")
    @Mapping(target = "idImage", source = "image.idImage")
    ProductExtra toProductExtra(ProductExtraEntity extra);

}
