package com.mdmc.posofmyheart.infrastructure.persistence.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.mdmc.posofmyheart.domain.models.DataCatalogs;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

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
            productSauceRepository.saveAll(data.productSauces());
        }
        if (productExtraRepository.count() == 0) {
            productExtraRepository.saveAll(data.productExtras());
        }
        if (productRepository.count() == 0) {
            productRepository.saveAll(data.products());
        }
        if (productFlavorRepository.count() == 0) {
            productFlavorRepository.saveAll(data.productFlavors());
        }
        if (productVariantRepository.count() == 0) {
            productVariantRepository.saveAll(data.productVariants());
        }
    }
}