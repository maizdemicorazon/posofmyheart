package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.Sauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.SauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ProductMapper.class, ProductExtraMapper.class, SauceMapper.class})
public interface ProductsWithExtrasMapper {
    ProductsWithExtrasMapper INSTANCE = Mappers.getMapper(ProductsWithExtrasMapper.class);

    // Método alternativo si necesitas mapear desde entidades directamente
    ProductsWithExtrasDto toDtoFromEntities(List<ProductEntity> products,
                                            List<ProductExtraEntity> extras,
                                            List<SauceEntity> sauces);
}