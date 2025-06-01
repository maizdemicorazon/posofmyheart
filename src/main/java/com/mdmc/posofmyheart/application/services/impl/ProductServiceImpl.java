package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductMapper;
import com.mdmc.posofmyheart.application.mappers.ProductsWithExtrasMapper;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.SauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.SauceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductExtraRepository productExtraRepository;
    private final SauceRepository sauceRepository;

    @Override
    public Product getProductById(Long idProduct) {
        return ProductMapper.INSTANCE.toDomain(
                productRepository.findById(idProduct)
                .orElseThrow(ProductNotFoundException::new)
        );
    }

    @Override
    public ProductsWithExtrasDto getMenuProducts() {
        List<ProductEntity> products = productRepository.findAll();
        List<ProductExtraEntity> extras = productExtraRepository.findAll();
        List<SauceEntity> sauces = sauceRepository.findAll();
        //TODO Add PayMethods

        return ProductsWithExtrasMapper.INSTANCE.toDtoFromEntities(products, extras, sauces);
    }

}
