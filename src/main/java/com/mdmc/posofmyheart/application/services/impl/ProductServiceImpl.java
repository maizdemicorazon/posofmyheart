package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.mappers.ProductExtraMapper;
import com.mdmc.posofmyheart.application.mappers.ProductMapper;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Category;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductExtraRepository productExtraRepository;

    @Cacheable("menu")
    public ProductsWithExtrasDto getMenuProducts() {
        List<Product> products = repository.findAll()
                .stream()
                .map(product -> new Product(
                        product.getIdProduct(),
                        Category.fromEntity(
                                product.getCategory()
                        ).idCategory(),
                        product.getName(),
                        product.getImage(),
                        ProductMapper.INSTANCE.toDomainVariants(
                                product.getVariants()
                        )
                ))
                .toList();

        List<ProductExtra> extras = ProductExtraMapper.INSTANCE.toDomainExtras(
                productExtraRepository.findAll()
        );

        return ProductMapper.INSTANCE.toDto(products, extras);
    }
}