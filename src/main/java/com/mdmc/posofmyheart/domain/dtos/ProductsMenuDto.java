package com.mdmc.posofmyheart.domain.dtos;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.ProductSauce;

import java.util.List;

public record ProductsMenuDto(
        List<Product> products,
        List<ProductExtra> extras,
        List<ProductSauce> sauces,
        List<PaymentMethod> paymentMethods
) {
}