package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductMapper;
import com.mdmc.posofmyheart.application.mappers.ProductMenuDtoMapper;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsMenuDto;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.mappers.PaymentMethodEntityMapper;
import com.mdmc.posofmyheart.infrastructure.persistence.mappers.ProductEntityMapper;
import com.mdmc.posofmyheart.infrastructure.persistence.mappers.ProductExtrasEntityMapper;
import com.mdmc.posofmyheart.infrastructure.persistence.mappers.ProductSauceEntityMapper;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductSauceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductExtraRepository productExtraRepository;
    private final ProductSauceRepository productSauceRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public Product getProductById(Long idProduct) {
        return ProductMapper.INSTANCE.toProduct(
                productRepository.findById(idProduct)
                .orElseThrow(()-> new ProductNotFoundException(idProduct))
        );
    }

    @Override
    public ProductsMenuDto getMenuProducts() {
        List<ProductEntity> products = ProductEntityMapper.INSTANCE
                .toProductsOrderedById(productRepository.findAll());
        List<ProductExtraEntity> extras = ProductExtrasEntityMapper.INSTANCE
                .toExtrasOrderedById(productExtraRepository.findAll());
        List<ProductSauceEntity> sauces = ProductSauceEntityMapper.INSTANCE
                .toExtrasOrderedById(productSauceRepository.findAll());
        List<PaymentMethodEntity> paymentMethods = PaymentMethodEntityMapper.INSTANCE
                .toPaymentsOrderedById(paymentMethodRepository.findAll());

        return ProductMenuDtoMapper.INSTANCE.toProductsMenu(products, extras, sauces, paymentMethods);
    }

}
