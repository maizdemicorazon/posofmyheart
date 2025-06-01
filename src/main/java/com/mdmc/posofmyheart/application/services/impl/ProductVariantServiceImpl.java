package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.VariantNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductVariantMapper;
import com.mdmc.posofmyheart.application.services.ProductVariantService;
import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;

    @Override
    public List<ProductVariant> getAllVariants() {
        return ProductVariantMapper.INSTANCE.toDomainVariants(variantRepository.findAll());
    }

    @Override
    public ProductVariant getVariantById(Long id) {
        return ProductVariantMapper.INSTANCE.toModel(
                variantRepository.findById(id)
                        .orElseThrow(VariantNotFoundException::new)
        );
    }

}