package com.mdmc.posofmyheart.domain.patterns.facade;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;

import java.util.Map;

public record PreloadedEntities(
        Map<Long, ProductExtraEntity> extras,
        Map<Long, ProductSauceEntity> sauces,
        Map<Long, ProductFlavorEntity> flavors
) {}
