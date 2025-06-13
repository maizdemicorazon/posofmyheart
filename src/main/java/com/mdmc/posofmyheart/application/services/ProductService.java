package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.dtos.ProductsMenuDto;
import com.mdmc.posofmyheart.domain.models.Product;

public interface ProductService {
    Product getProductById(Long idProduct);
    ProductsMenuDto getMenuProducts();
}
