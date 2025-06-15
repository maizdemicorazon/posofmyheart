package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.models.ProductFlavor;

import java.util.List;

public interface ProductFlavorService {
    ProductFlavor getFlavorById(Long idFlavor);

    List<ProductFlavor> getAllFlavors();

    List<ProductFlavor> getFlavorsByProductId(Long productId);

    List<ProductFlavor> getActiveFlavorsByProductId(Long productId);

    boolean isFlavorAvailableForProduct(Long productId, Long flavorId);
}
