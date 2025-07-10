package com.mdmc.posofmyheart.infrastructure.configurations;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.mdmc.posofmyheart.application.services.ProductImageService;
import com.mdmc.posofmyheart.domain.models.DataCatalogs;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductCategoryRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductFlavorRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductSauceRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductVariantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@AllArgsConstructor
@Log4j2
@Profile("!prod")
public class DataInitializer implements ApplicationRunner {

    public static final String DATA_CATALOGS_JSON_PATH = "data_catalogs.json";
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductExtraRepository productExtraRepository;
    private final ProductSauceRepository productSauceRepository;
    private final ProductRepository productRepository;
    private final ProductFlavorRepository productFlavorRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageService productImageService;

    List<ProductEntity> products;
    List<ProductSauceEntity> sauces;
    List<ProductExtraEntity> extras;
    List<ProductFlavorEntity> flavors;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("Inicializando datos desde JSON...");
        try {
            initializeDataFromJson(); // Ruta del archivo JSON
        } catch (IOException e) {
            log.error("Error al cargar datos desde JSON", e);
        }
        log.info("Inicialización completada.");
    }

    private void initializeDataFromJson() throws IOException {

        InputStream inputStream = new ClassPathResource(DATA_CATALOGS_JSON_PATH).getInputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        DataCatalogs data = objectMapper.readValue(inputStream, DataCatalogs.class);

        if (paymentMethodRepository.count() == 0) {
            paymentMethodRepository.saveAll(data.paymentMethods());
        }
        if (productCategoryRepository.count() == 0) {
            productCategoryRepository.saveAll(data.productCategories());
        }
        if (productSauceRepository.count() == 0) {
            sauces = data.productSauces();
            productSauceRepository.saveAll(sauces);
        }
        if (productExtraRepository.count() == 0) {
            extras = data.productExtras();
            productExtraRepository.saveAll(extras);
        }
        if (productRepository.count() == 0) {
            products = data.products();
            productRepository.saveAll(products);
        }
        if (productFlavorRepository.count() == 0) {
            flavors = data.productFlavors();
            productFlavorRepository.saveAll(flavors);
        }
        if (productVariantRepository.count() == 0) {
            productVariantRepository.saveAll(data.productVariants());
        }

        products.stream()
       .collect(Collectors.toMap(ProductEntity::getIdProduct, ProductEntity::getLocalResource))
       .forEach(productImageService::uploadImageToEntityFromResources);


//        Map<Long, String> sauceImageMap = sauces.stream()
//                .collect(Collectors.toMap(ProductSauceEntity::getIdSauce, ProductSauceEntity::getLocalResource));
//        imageBatchService.loadImagesFromResources(sauceImageMap);
//        Map<Long, String> extrasImageMap = extras.stream()
//                .collect(Collectors.toMap(ProductExtraEntity::getIdExtra, ProductExtraEntity::getLocalResource));
//        imageBatchService.loadImagesFromResources(extrasImageMap);

//        Map<Long, String> flavorsImageMap = flavors.stream()
//                .collect(Collectors.toMap(ProductFlavorEntity::getIdFlavor, ProductFlavorEntity::getLocalResource));
//        imageBatchService.loadImagesFromResources(flavorsImageMap);
    }

}