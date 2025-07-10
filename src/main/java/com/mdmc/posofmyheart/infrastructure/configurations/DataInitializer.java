package com.mdmc.posofmyheart.infrastructure.configurations;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.mdmc.posofmyheart.application.services.CatalogImageService;
import com.mdmc.posofmyheart.domain.models.DataCatalogs;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.CatalogImageRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductCategoryRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductFlavorRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductSauceRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Log4j2
@Profile("!prod")
public class DataInitializer implements ApplicationRunner {

    public static final String DATA_CATALOGS_JSON_PATH = "data_catalogs.json";

    // Repositorios
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductExtraRepository productExtraRepository;
    private final ProductSauceRepository productSauceRepository;
    private final ProductRepository productRepository;
    private final ProductFlavorRepository productFlavorRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CatalogImageRepository catalogImageRepository;
    private final Object saveLock = new Object();

    // Servicios
    private final CatalogImageService catalogImageService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("🚀 Inicializando datos desde JSON...");
        try {
            DataCatalogs data = loadDataFromJson();

            // 1. Cargar imágenes primero
            loadCatalogImages(data);

            // 2. Cargar datos básicos
            loadBasicData(data);

//            // 3. Establecer relaciones con imágenes
            establishImageRelations(data);

            log.info("✅ Inicialización completada exitosamente.");

        } catch (IOException e) {
            log.error("❌ Error al cargar datos desde JSON", e);
            throw new RuntimeException("Error en la inicialización de datos", e);
        }
    }

    private DataCatalogs loadDataFromJson() throws IOException {
        InputStream inputStream = new ClassPathResource(DATA_CATALOGS_JSON_PATH).getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper.readValue(inputStream, DataCatalogs.class);
    }

    private void loadCatalogImages(DataCatalogs data) {
        log.info("📷 Cargando {} imágenes del catálogo...", data.catalogImages().size());
        List<CatalogImageEntity> processedImages = data.catalogImages()
                .parallelStream()
                .map(imageData -> {
                    try {
                        CatalogImageEntity image = catalogImageService.processImageFromResource(
                                imageData.getResource(),
                                imageData.getImageType(),
                                imageData.getFileName(),
                                imageData.getAltText()
                        );
                        image.setIdImage(imageData.getIdImage());
                        log.debug("✅ Imagen procesada: {} -> ID: {}", imageData.getFileName(), image.getIdImage());
                        return image; // ✅ No guardar aquí
                    } catch (Exception e) {
                        log.error("❌ Error procesando imagen {}: {}", imageData.getFileName(), e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        // ✅ Guardar todas en una sola operación batch
        List<CatalogImageEntity> savedImages = catalogImageRepository.saveAll(processedImages);

        log.info("✅ Procesadas: {} imágenes, Guardadas: {}",
                processedImages.size(), savedImages.size());

    }

    private void loadBasicData(DataCatalogs data) {
        log.info("📦 Cargando datos básicos...");

        // Cargar entidades sin relaciones de imagen primero
        productCategoryRepository.saveAll(data.productCategories());
        productRepository.saveAll(data.products());
        paymentMethodRepository.saveAll(data.paymentMethods());
        productExtraRepository.saveAll(data.productExtras());
        productSauceRepository.saveAll(data.productSauces());
        productFlavorRepository.saveAll(data.productFlavors());
        productVariantRepository.saveAll(data.productVariants());

        log.info("📦 Datos básicos cargados exitosamente");
    }

    private void establishImageRelations(DataCatalogs data) {
        log.info("🔗 Estableciendo relaciones con imágenes...");

        // Crear mapas para búsqueda eficiente
        Map<Long, CatalogImageEntity> imageMap = catalogImageRepository.findAll()
                .stream()
                .collect(Collectors.toMap(CatalogImageEntity::getIdImage, Function.identity()));

        // Establecer relaciones para productos
        List<ProductEntity> processedProducts = data.products().stream()
                .filter(product -> product.getImage().getIdImage() != null)
                .map(product -> {
                    CatalogImageEntity image = imageMap.get(product.getImage().getIdImage());
                    if (image != null) {
                        product.setImage(image);
                        productRepository.save(product);
                        return product; // Producto actualizado
                    } else {
                        log.warn("⚠️ Imagen no encontrada para producto {}: ID imagen {}",
                                product.getIdProduct(), product.getImage().getIdImage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        long totalProcessed = data.products().stream()
                .filter(product -> product.getImage().getIdImage() != null)
                .count();

        log.info("✅ Procesados: {} productos, Actualizados: {}",
                totalProcessed, processedProducts.size());

        // Establecer relaciones para extras
        List<ProductExtraEntity> processedExtras = data.productExtras().stream()
                .filter(extra -> extra.getImage().getIdImage() != null)
                .map(extra -> {
                    CatalogImageEntity image = imageMap.get(extra.getImage().getIdImage());
                    if (image != null) {
                        extra.setImage(image);
                        productExtraRepository.save(extra);
                        return extra;
                    } else {
                        log.warn("⚠️ Imagen no encontrada para extra {}: ID imagen {}",
                                extra.getIdExtra(), extra.getImage().getIdImage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        totalProcessed = data.productExtras().stream()
                .filter(extra -> extra.getImage().getIdImage() != null)
                .count();
        log.info("✅ Procesados: {} extras, Actualizados: {}",
                totalProcessed, processedExtras.size());

        // Establecer relaciones para salsas
        List<ProductSauceEntity> processedSauces = data.productSauces().stream()
                .filter(sauce -> sauce.getImage().getIdImage() != null)
                .map(sauce -> {
                    CatalogImageEntity image = imageMap.get(sauce.getImage().getIdImage());
                    if (image != null) {
                        sauce.setImage(image);
                        productSauceRepository.save(sauce);
                        return sauce;
                    } else {
                        log.warn("⚠️ Imagen no encontrada para salsa {}: ID imagen {}",
                                sauce.getIdSauce(), sauce.getImage().getIdImage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        totalProcessed = data.productSauces().stream()
                .filter(sauce -> sauce.getImage().getIdImage() != null)
                .count();
        log.info("✅ Procesados: {} sauces, Actualizados: {}",
                totalProcessed, processedSauces.size());

        // Establecer relaciones para sabores
        List<ProductFlavorEntity> processedFlavors = data.productFlavors().stream()
                .filter(flavor -> flavor.getImage().getIdImage() != null)
                .map(flavor -> {
                    CatalogImageEntity image = imageMap.get(flavor.getImage().getIdImage());
                    if (image != null) {
                        flavor.setImage(image);
                        productFlavorRepository.save(flavor);
                        return flavor;
                    } else {
                        log.warn("⚠️ Imagen no encontrada para sabor {}: ID imagen {}",
                                flavor.getIdFlavor(), flavor.getImage().getIdImage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        totalProcessed = data.productFlavors().stream()
                .filter(flavor -> flavor.getImage().getIdImage() != null)
                .count();
        log.info("✅ Procesados: {} flavors, Actualizados: {}",
                totalProcessed, processedFlavors.size());

        log.info("🔗 Relaciones con imágenes establecidas exitosamente");
    }
}