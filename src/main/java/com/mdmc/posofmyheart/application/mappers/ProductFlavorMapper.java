package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductFlavor;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CatalogImageMapper.class})
public interface ProductFlavorMapper {

    ProductFlavorMapper INSTANCE = Mappers.getMapper(ProductFlavorMapper.class);

    @Mapping(target = "idImage", source = "image.idImage")
    ProductFlavor toProductFlavor(ProductFlavorEntity flavorEntity);

}
