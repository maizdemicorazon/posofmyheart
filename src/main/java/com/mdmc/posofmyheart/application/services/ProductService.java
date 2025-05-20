package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Product;

public interface ProductService {
    Product getProductById(Long idProduct);
    ProductsWithExtrasDto getMenuProducts();
}
