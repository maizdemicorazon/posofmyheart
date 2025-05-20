package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.models.ProductVariant;
import java.util.List;

public interface ProductVariantService {
    List<ProductVariant> getAllVariants();
    ProductVariant getVariantById(Long id);
}