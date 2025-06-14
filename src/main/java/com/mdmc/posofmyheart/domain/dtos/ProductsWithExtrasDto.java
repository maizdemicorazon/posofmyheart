package com.mdmc.posofmyheart.domain.dtos;

import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.Sauce;

import java.util.List;

public record ProductsWithExtrasDto(
        List<Product> products,
        List<ProductExtra> extras,
        List<Sauce> sauces
) {
}