package com.mdmc.posofmyheart.application.services;

import java.util.List;

import com.mdmc.posofmyheart.domain.models.ProductFlavor;

public interface ProductFlavorService {
    ProductFlavor getFlavorById(Long idFlavor);

    List<ProductFlavor> getAllFlavors();

    List<ProductFlavor> getFlavorsByProductId(Long productId);

    List<ProductFlavor> getActiveFlavorsByProductId(Long productId);

    boolean isFlavorAvailableForProduct(Long productId, Long flavorId);
}
