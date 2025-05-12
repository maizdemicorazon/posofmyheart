package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Product;

import java.util.List;

public interface ProductService {
    ProductsWithExtrasDto getMenuProducts();
}
