package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.FlavorNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductFlavorMapper;
import com.mdmc.posofmyheart.application.services.ProductFlavorService;
import com.mdmc.posofmyheart.domain.models.ProductFlavor;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductFlavorRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductFlavorServiceImpl implements ProductFlavorService {

    private final ProductFlavorRepository flavorRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductFlavor> getAllFlavors() {
        log.debug("Getting all flavors");
        return ProductFlavorMapper.INSTANCE.toProductFlavors(flavorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductFlavor getFlavorById(Long idFlavor) {
        log.debug("Getting flavor by id: {}", idFlavor);
        return ProductFlavorMapper.INSTANCE.toProductFlavor(
                flavorRepository.findById(idFlavor)
                        .orElseThrow(() -> new FlavorNotFoundException(idFlavor))
        );
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "flavors", key = "'product_' + #productId")
    public List<ProductFlavor> getFlavorsByProductId(Long productId) {
        log.debug("Getting all flavors for product id: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return flavorRepository.findAllFlavorsByProductIdWithProduct(productId)
                .stream()
                .map(ProductFlavorMapper.INSTANCE::toProductFlavor)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "flavors", key = "'active_product_' + #productId")
    public List<ProductFlavor> getActiveFlavorsByProductId(Long productId) {
        log.debug("Getting active flavors for product id: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return flavorRepository.findActiveFlavorsByProductIdWithProduct(productId)
                .stream()
                .map(ProductFlavorMapper.INSTANCE::toProductFlavor)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFlavorAvailableForProduct(Long productId, Long flavorId) {
        log.debug("Checking if flavor {} is available for product {}", flavorId, productId);

        // Verificar que ambos existen
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        flavorRepository.findById(flavorId)
                .orElseThrow(() -> new FlavorNotFoundException(flavorId));

        return flavorRepository.existsByProductIdAndFlavorId(productId, flavorId);
    }
}