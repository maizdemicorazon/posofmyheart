package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.models.Category;
import com.mdmc.posofmyheart.domain.models.Price;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public List<Product> getMenuProducts() {
        return repository.findAll()
                .stream()
                .map(product -> new Product(
                        product.getId(),
                        Category.fromEntity(product.getCategory()),
                        product.getName(),
                        product.getDescription(),
                        product.getSize(),
                        Price.to(product.getPrices()).orElseThrow().sellPrice()
                ))
                .toList();
    }
}