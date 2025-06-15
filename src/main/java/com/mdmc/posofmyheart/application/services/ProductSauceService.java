package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.models.ProductSauce;

import java.util.List;

public interface ProductSauceService {
    ProductSauce getSauceById(Long idSauce);

    List<ProductSauce> getAllSauces();
}
