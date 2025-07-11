package com.mdmc.posofmyheart.application.services;

import java.util.List;

import com.mdmc.posofmyheart.domain.models.ProductSauce;

public interface ProductSauceService {
    ProductSauce getSauceById(Long idSauce);

    List<ProductSauce> getAllSauces();
}
