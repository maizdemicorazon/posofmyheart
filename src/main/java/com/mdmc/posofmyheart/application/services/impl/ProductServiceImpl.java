package com.mdmc.posofmyheart.application.services.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductMapper;
import com.mdmc.posofmyheart.application.mappers.ProductMenuDtoMapper;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsMenuDto;
import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.mappers.ProductEntityMapper;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductSauceRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductExtraRepository productExtraRepository;
    private final ProductSauceRepository productSauceRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'product-' + #idProduct")
    public Product getProductById(Long idProduct) {
        log.debug("🔍 Obteniendo producto por ID: {} con todas las relaciones e imágenes", idProduct);

        long startTime = System.currentTimeMillis();

        // Query optimizada que carga producto con todas sus relaciones e imágenes
        ProductEntity productEntity = productRepository.findByIdWithAllRelationsAndImages(idProduct)
                .orElseThrow(() -> new ProductNotFoundException(idProduct));

        Product product = ProductMapper.INSTANCE.toProduct(productEntity);

        long endTime = System.currentTimeMillis();
        log.info("✅ Producto {} obtenido con imágenes en {}ms", idProduct, (endTime - startTime));

        return product;
    }

    /**
     * getMenuProducts() con consultas optimizadas que incluyen imágenes
     * UNA SOLA QUERY por tipo con LEFT JOIN FETCH para imágenes
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'menu'")
    public ProductsMenuDto getMenuProducts() {
        log.debug("🔍 Obteniendo menú completo con imágenes optimizado");

        long startTime = System.currentTimeMillis();

        // 1. Productos con todas las relaciones e imágenes (incluyendo sabores)
        List<ProductEntity> products = ProductEntityMapper.INSTANCE.toProductsByIdAsc(
                productRepository.findByIdWithAllRelations()
        );

        // 2. Extras con imágenes
        List<ProductExtra> extras = productExtraRepository.findAllActive()
                .stream()
                .map(entity -> new ProductExtra(
                        entity.getIdExtra(),
                        entity.getName(),
                        entity.getActualPrice(),
                        entity.getImage().getIdImage()
                ))
                .toList();

        // 3. Salsas con imágenes
        List<ProductSauce> sauces = productSauceRepository.findAllActiveSauces()
                .stream()
                .map(entity -> new ProductSauce(
                        entity.getIdSauce(),
                        entity.getName(),
                        entity.getImage().getIdImage()
                ))
                .toList();

        // 4. Métodos de pago (sin imágenes)
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAllPaymentMethods();

        ProductsMenuDto menu = ProductMenuDtoMapper.INSTANCE
                .toProductsMenu(products, extras, sauces, paymentMethods);

        long endTime = System.currentTimeMillis();
        log.info("✅ Menú completo con imágenes obtenido en {}ms - {} productos, {} extras, {} salsas",
                (endTime - startTime), products.size(), extras.size(), sauces.size());

        return menu;
    }
}