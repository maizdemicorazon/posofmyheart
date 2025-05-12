package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductExtraMapper {

    ProductExtraMapper INSTANCE = Mappers.getMapper(ProductExtraMapper.class);

    @Mapping(target = "id", source = "idExtra")
    ProductExtra toDomainExtra(ProductExtraEntity extra);

    default List<ProductExtra> toDomainExtras(List<ProductExtraEntity> variants){
        return variants.stream()
                .map(this::toDomainExtra)
                .sorted(Comparator.comparing(ProductExtra::price))
                .collect(Collectors.toList());
    }

}
