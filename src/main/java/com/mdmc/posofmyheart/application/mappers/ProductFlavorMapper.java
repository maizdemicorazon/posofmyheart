package com.mdmc.posofmyheart.application.mappers;

import java.util.List;

import com.mdmc.posofmyheart.domain.models.ProductFlavor;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CatalogImageMapper.class})
public interface ProductFlavorMapper {

    ProductFlavorMapper INSTANCE = Mappers.getMapper(ProductFlavorMapper.class);

    @Mapping(target = "image", source = "image", qualifiedByName = "catalogImageToByteArray")
    ProductFlavor toProductFlavor(ProductFlavorEntity flavorEntity);

    List<ProductFlavor> toProductFlavors(List<ProductFlavorEntity> flavorEntities);
}