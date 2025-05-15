package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "price", source = "sellPrice")
    ProductVariant toDomainVariant(ProductVariantEntity variant);

    default List<ProductVariant> toDomainVariants(List<ProductVariantEntity> variants){
        return variants.stream()
                .map(this::toDomainVariant)
                .sorted(Comparator.comparing(ProductVariant::price))
                .collect(Collectors.toList());
    }

    ProductsWithExtrasDto toDto(List<Product> products, List<ProductExtra> extras);

}
