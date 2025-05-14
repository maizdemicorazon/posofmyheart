package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.Category;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = getMapper(CategoryMapper.class);

    Category fromEntity(ProductCategoryEntity entity);
}
