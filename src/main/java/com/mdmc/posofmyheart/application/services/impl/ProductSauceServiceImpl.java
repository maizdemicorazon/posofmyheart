package com.mdmc.posofmyheart.application.services.impl;

import java.util.List;

import com.mdmc.posofmyheart.api.exceptions.ProductSauceNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductSauceMapper;
import com.mdmc.posofmyheart.application.services.ProductSauceService;
import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductSauceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductSauceServiceImpl implements ProductSauceService {

    private final ProductSauceRepository productSauceRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "sauces", key = "'sauce-' + #idSauce")
    public ProductSauce getSauceById(Long idSauce) {
        log.debug("🔍 Obteniendo salsa por ID: {}", idSauce);

        return productSauceRepository.findById(idSauce)
                .map(ProductSauceMapper.INSTANCE::toProductSauce)
                .orElseThrow(() -> new ProductSauceNotFoundException(idSauce));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "sauces", key = "'all-sauces'")
    public List<ProductSauce> getAllSauces() {
        log.debug("🔍 Obteniendo todas las salsas con imágenes");

        long startTime = System.currentTimeMillis();

        // Usar consulta optimizada que carga las imágenes de una vez
        List<ProductSauce> sauces = productSauceRepository.findActiveWithImages()
                .stream()
                .map(ProductSauceMapper.INSTANCE::toProductSauce)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} salsas obtenidas con imágenes en {}ms", sauces.size(), (endTime - startTime));

        return sauces;
    }
}