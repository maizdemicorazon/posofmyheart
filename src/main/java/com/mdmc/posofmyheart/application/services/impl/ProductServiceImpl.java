package com.mdmc.posofmyheart.application.services.impl;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductExtraRepository productExtraRepository;
    private final ProductSauceRepository productSauceRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    /**
     * getMenuProducts() con UNA SOLA QUERY por tipo
     * Elimina múltiples findAll() - MEJORA CRÍTICA DE PERFORMANCE
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'menu'")
    public ProductsMenuDto getMenuProducts() {
        log.debug("🔍 Obteniendo menú completo con optimización EntityGraph");

        long startTime = System.currentTimeMillis();

        List<ProductEntity> products = ProductEntityMapper.INSTANCE.toProductsByIdAsc(
                productRepository.findByIdWithAllRelations()
        );

        List<ProductExtra> extras = productExtraRepository.findAllExtras();

        List<ProductSauce> sauces = productSauceRepository.findAllSauces();

        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAllPaymentMethods();

        ProductsMenuDto menu = ProductMenuDtoMapper.INSTANCE
                .toProductsMenu(products, extras, sauces, paymentMethods);

        long endTime = System.currentTimeMillis();
        log.info("✅ Menú completo obtenido en {}ms", (endTime - startTime));

        return menu;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'product-' + #idProduct")
    public Product getProductById(Long idProduct) {
        log.debug("🔍 Obteniendo producto por ID: {} con EntityGraph", idProduct);

        long startTime = System.currentTimeMillis();

        // Query optimizada con EntityGraph completo
        ProductEntity productEntity = productRepository.findById(idProduct)
                .orElseThrow(() -> new ProductNotFoundException(idProduct));

        Product product = ProductMapper.INSTANCE.toProduct(productEntity);

        long endTime = System.currentTimeMillis();
        log.info("✅ Producto {} obtenido en {}ms", idProduct, (endTime - startTime));

        return product;
    }

}