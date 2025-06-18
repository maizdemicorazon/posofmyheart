package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.dtos.ProductsMenuDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ProductMapper.class, ProductExtraMapper.class, PaymentMethodMapper.class})
public interface ProductMenuDtoMapper {
    ProductMenuDtoMapper INSTANCE = Mappers.getMapper(ProductMenuDtoMapper.class);

    ProductsMenuDto toProductsMenu(List<ProductEntity> products,
                                   List<ProductExtraEntity> extras,
                                   List<ProductSauceEntity> sauces,
                                   List<PaymentMethodEntity> paymentMethods);
}