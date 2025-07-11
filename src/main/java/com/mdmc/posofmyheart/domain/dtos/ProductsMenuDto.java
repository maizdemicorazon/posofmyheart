package com.mdmc.posofmyheart.domain.dtos;

import java.util.List;

import lombok.Builder;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.ProductSauce;

@Builder
public record ProductsMenuDto(
        List<Product> products,
        List<ProductExtra> extras,
        List<ProductSauce> sauces,
        List<PaymentMethod> paymentMethods
) {
}