package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.mappers.CategoryMapper;
import com.mdmc.posofmyheart.application.mappers.ProductExtraMapper;
import com.mdmc.posofmyheart.application.mappers.ProductMapper;
import com.mdmc.posofmyheart.application.mappers.SauceMapper;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.Sauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
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

    private final ProductRepository repository;
    private final ProductExtraRepository productExtraRepository;
    private final SauceRepository sauceRepository;

    public ProductsWithExtrasDto getMenuProducts() {
        List<Product> products = repository.findAll()
                .stream()
                .map(product -> new Product(
                                product.getIdProduct(),
                                getIdCategory(product),
                                product.getName(),
                                product.getImage(),
                                ProductMapper.INSTANCE.toDomainVariants(
                                        product.getVariants()
                                )
                        )
                )
                .toList();

        List<ProductExtra> extras = ProductExtraMapper.INSTANCE.toDomainExtras(
                productExtraRepository.findAll()
        );

        List<Sauce> sauces = SauceMapper.INSTANCE.toDomainSauces(
                sauceRepository.findAll()
        );

        return ProductMapper.INSTANCE.toDto(products, extras, sauces);
    }

    private static Long getIdCategory(ProductEntity product) {
        return CategoryMapper.INSTANCE
                .fromEntity(
                        product.getCategory()
                )
                .idCategory();
    }
}