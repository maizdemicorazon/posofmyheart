package com.mdmc.posofmyheart.application.services.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.api.exceptions.ProductExtraNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductExtraMapper;
import com.mdmc.posofmyheart.application.services.ProductExtraService;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductExtraServiceImpl implements ProductExtraService {

    private final ProductExtraRepository productExtraRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "extras", key = "'extra-' + #idExtra")
    public ProductExtra getExtraById(Long idExtra) {
        log.debug("🔍 Obteniendo extra por ID: {}", idExtra);

        return productExtraRepository.findById(idExtra)
                .map(ProductExtraMapper.INSTANCE::toProductExtra)
                .orElseThrow(() -> new ProductExtraNotFoundException(idExtra));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "extras", key = "'all-extras'")
    public List<ProductExtra> getAllExtras() {
        log.debug("🔍 Obteniendo todos los extras con imágenes");

        long startTime = System.currentTimeMillis();

        // Usar consulta optimizada que carga las imágenes de una vez
        List<ProductExtra> extras = productExtraRepository.findAllActiveWithImages()
                .stream()
                .map(ProductExtraMapper.INSTANCE::toProductExtra)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} extras obtenidos con imágenes en {}ms", extras.size(), (endTime - startTime));

        return extras;
    }
}