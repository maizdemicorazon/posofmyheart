package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ProductSauceNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductSauceMapper;
import com.mdmc.posofmyheart.application.services.ProductSauceService;
import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductSauceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductSauceServiceImpl implements ProductSauceService {

    private ProductSauceRepository productSauceRepository;

    @Override
    public ProductSauce getSauceById(Long idSauce) {
        return ProductSauceMapper.INSTANCE.toProductSauce(
                productSauceRepository.findById(idSauce)
                        .orElseThrow(() -> new ProductSauceNotFoundException(idSauce))
        );
    }

    @Override
    public List<ProductSauce> getAllSauces() {
        return ProductSauceMapper.INSTANCE.toProductSauces(productSauceRepository.findAll());
    }
}
