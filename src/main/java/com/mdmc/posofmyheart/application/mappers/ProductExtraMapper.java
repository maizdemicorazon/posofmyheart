package com.mdmc.posofmyheart.application.mappers;

import java.util.List;

import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CatalogImageMapper.class})
public interface ProductExtraMapper {

    ProductExtraMapper INSTANCE = Mappers.getMapper(ProductExtraMapper.class);

    @Mapping(target = "price", source = "actualPrice")
    @Mapping(target = "image", source = "image", qualifiedByName = "catalogImageToByteArray")
    ProductExtra toProductExtra(ProductExtraEntity extra);

    List<ProductExtra> toProductExtras(List<ProductExtraEntity> extras);
}