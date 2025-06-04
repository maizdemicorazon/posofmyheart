package com.mdmc.posofmyheart.infrastructure.persistence.configurations;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Log4j2
@Profile("!prod")
public class DataInitializer implements ApplicationRunner {

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
        initializeData();
    }

    private void initializeData() {
        log.info("Inicializando datos básicos...");

        if (paymentMethodRepository.count() == 0) {
            initializePaymentMethods();
        }
        if (productCategoryRepository.count() == 0) {
            initializeProductCategories();
        }
        if (productSauceRepository.count() == 0) {
            initializeProductSauces();
        }
        if (productExtraRepository.count() == 0) {
            initializeProductExtras();
        }
        if (productRepository.count() == 0) {
            initializeProducts();
        }
        if (productFlavorRepository.count() == 0) {
            initializeProductFlavors();
        }
        if (productVariantRepository.count() == 0) {
            initializeProductVariants();
        }

        log.info("Inicialización de datos completada.");
    }

    private void initializePaymentMethods() {
        List<PaymentMethodEntity> methods = Arrays.asList(
                new PaymentMethodEntity(1L, "Efectivo", "Pago en efectivo", true, LocalDateTime.now()),
                new PaymentMethodEntity(2L, "Tarjeta", "Pago con tarjeta mediante terminal", true, LocalDateTime.now()),
                new PaymentMethodEntity(3L, "Transferencia", "Pago por transferencia bancaria", true, LocalDateTime.now()),
                new PaymentMethodEntity(4L, "QR", "QR de pago a través de Mercado Pago", true, LocalDateTime.now()),
                new PaymentMethodEntity(5L, "Link", "Link de pago a través de Mercado Pago", true, LocalDateTime.now())
        );
        paymentMethodRepository.saveAll(methods);
    }

    private void initializeProductCategories() {

        List<ProductCategoryEntity> categories = Arrays.asList(
                new ProductCategoryEntity(1L, "Esquites", "Variedad de esquites tradicionales y especiales"),
                new ProductCategoryEntity(2L, "Elotes", "Elotes preparados de diferentes formas"),
                new ProductCategoryEntity(3L, "Bebidas", "Refrescos, jugos y aguas"),
                new ProductCategoryEntity(4L, "De la casa", "Platillo de la casa"),
                new ProductCategoryEntity(5L, "Botana", "Papas sin maíz")
        );
        productCategoryRepository.saveAll(categories);
        log.info("Categorías de productos inicializadas");

    }

    private void initializeProductSauces() {
        List<ProductSauceEntity> sauces = Arrays.asList(
                new ProductSauceEntity(1L, "Sin picante", "Sin salsa picante",
                        "https://drive.google.com/thumbnail?id=11WPxG1tmzSVpZEN4PgtKNKwHhjxIBFu2"),
                new ProductSauceEntity(2L, "Buffalo", "Salsa picante",
                        "https://drive.google.com/thumbnail?id=1gwgeHvhMCIH4XaOk-5OITul1pwbamATX"),
                new ProductSauceEntity(3L, "Valentina", "Salsa Valentina clásica",
                        "https://drive.google.com/thumbnail?id=1CK-YPGAvKTzpkaZMXVYORJvCKhQFfr52"),
                new ProductSauceEntity(4L, "Habanero", "Salsa picante de habanero",
                        "https://drive.google.com/thumbnail?id=1p2dpvcMzfmLAzP7WzcOqsBIKQ9ogAXN1"),
                new ProductSauceEntity(5L, "Botanera", "Salsa para botanas",
                        "https://drive.google.com/thumbnail?id=1mMv8t1Wq4d7Yg9D08jZbRhby24mUUT6n"),
                new ProductSauceEntity(6L, "Salsas Negras", "Combinación de salsas negras",
                        "https://drive.google.com/thumbnail?id=1SBh-unGzG9sIWMGjQEbpga14yH3B5gJt"),
                new ProductSauceEntity(7L, "Macha", "Salsa de la casa",
                        "https://drive.google.com/thumbnail?id=18nJNjMam6IBnjGlswo2xbuLlqT9jdwHn"),
                new ProductSauceEntity(8L, "Tajin en polvo", "Tajin en polvo",
                        "https://drive.google.com/thumbnail?id=1xEFTsxtIS1dLJJriFYAPBz0elvZb9Kvs"),
                new ProductSauceEntity(9L, "Tajin alimonado", "Salsa alimonada picante",
                        "https://drive.google.com/thumbnail?id=10CwrVbBtCpMs4R8V1cPR499L8pJb2bbe"),
                new ProductSauceEntity(10L, "Tajin afrutado", "Salsa con sabor afrutado",
                        "https://drive.google.com/thumbnail?id=1vxLCJpliHgrAkdXjv07N6wXIxd1i8HSw")
        );
        productSauceRepository.saveAll(sauces);
    }

    private void initializeProductExtras() {
        List<ProductExtraEntity> extras = Arrays.asList(
                new ProductExtraEntity(1L,
                        "Queso Manchego Extra",
                        "Rebanada de queso extra",
                        BigDecimal.valueOf(10.00),
                        BigDecimal.valueOf(3.50),
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                ),
                new ProductExtraEntity(2L,
                        "Queso Amarillo Extra",
                        "50 ml Queso amarillo liquido extra",
                        BigDecimal.valueOf(10.00),
                        BigDecimal.valueOf(2.00),
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                ),
                new ProductExtraEntity(3L,
                        "Proteina extra",
                        "200g aprox. de proteina de puerco",
                        BigDecimal.valueOf(25.00),
                        BigDecimal.valueOf(12.00),
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                ),
                new ProductExtraEntity(4L,
                        "Sin extras",
                        "",
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                )
        );
        productExtraRepository.saveAll(extras);
    }

    private void initializeProducts() {
        // Primero obtenemos las categorías necesarias
        ProductCategoryEntity esquitesCategory = productCategoryRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Categoría Esquites no encontrada"));
        ProductCategoryEntity elotesCategory = productCategoryRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Categoría Elotes no encontrada"));
        ProductCategoryEntity bebidasCategory = productCategoryRepository.findById(3L)
                .orElseThrow(() -> new IllegalStateException("Categoría Bebidas no encontrada"));
        ProductCategoryEntity deLaCasaCategory = productCategoryRepository.findById(4L)
                .orElseThrow(() -> new IllegalStateException("Categoría Bebidas no encontrada"));
        ProductCategoryEntity botanasCategory = productCategoryRepository.findById(5L)
                .orElseThrow(() -> new IllegalStateException("Categoría Bebidas no encontrada"));

        List<ProductEntity> products = Arrays.asList(
                new ProductEntity(
                        1L,
                        esquitesCategory,
                        "Doriesquites",
                        "https://drive.google.com/thumbnail?id=1GmaqB_ifP0h-Ck6S_ENZi45PfH7tbwEq",
                        "Doritos con esquites",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        2L,
                        esquitesCategory,
                        "Tostiesquites",
                        "https://drive.google.com/thumbnail?id=1v2OT5_GYNYSsr7XaHqnsxEEax1jLtWit",
                        "Tostiesquites con esquites",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        3L,
                        esquitesCategory,
                        "Esquites Tradicionales",
                        "https://drive.google.com/thumbnail?id=1ph3Iog_GrUF6VY2DmiapwquOCUcTDTNx",
                        "Esquites con mayonesa, queso y picante",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        4L,
                        esquitesCategory,
                        "Esquites con Queso Manchego/Amarillo",
                        "https://drive.google.com/thumbnail?id=1ZG_sIFvIvUyBNK0x1as1WvDNAC2RIP10",
                        "Esquites tradicionales con queso extra",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        5L,
                        esquitesCategory,
                        "Esquite Maruchan",
                        "https://drive.google.com/thumbnail?id=1Hz-cFGIRsnYp4ynm8pJsoHB9oTRc8HYg",
                        "Sopa maruchan con esquites",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()),
                new ProductEntity(
                        6L,
                        deLaCasaCategory,
                        "Maíz Puerco",
                        "https://drive.google.com/thumbnail?id=1_DwAv9akn8dmrq-sO_PAAaAEEGPwPIF_",
                        "Maruchan con esquites y carne de puerco",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        7L,
                        deLaCasaCategory,
                        "Maíz Puerco sin sopa",
                        "https://drive.google.com/thumbnail?id=1_DwAv9akn8dmrq-sO_PAAaAEEGPwPIF_",
                        "Esquites con carne de puerco sin maruchan",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        8L,
                        elotesCategory,
                        "Elote Tradicional",
                        "https://drive.google.com/thumbnail?id=1R0z_q1JH1H7mlpSvbUt36pGbfGuUPmJk",
                        "Elote con mayonesa, queso y picante",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        9L,
                        elotesCategory,
                        "Elote Revolcado",
                        "https://drive.google.com/thumbnail?id=1i1r2iQQIfm0TsfbCJxxlEGahcmT5TwWc",
                        "Elote con topping a elegir",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        10L,
                        bebidasCategory,
                        "Red Cola 600ml",
                        "https://drive.google.com/thumbnail?id=1bDM3IJmOQDtl1xz1OquqH4UFiKU90J5d",
                        "Refresco en botella PET",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        11L,
                        bebidasCategory,
                        "Coca Cola",
                        "https://drive.google.com/thumbnail?id=1C41w2JUpvJleSgnrUiLR5s4jXek0bMUM",
                        "Refresco en botella PET o lata",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        12L,
                        bebidasCategory,
                        "Boing 500ml",
                        "https://drive.google.com/thumbnail?id=1PUk_tLBQoctGdNcG1MsbrGHEE6SONwTd",
                        "Jugo en caja 500ml",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        13L,
                        bebidasCategory,
                        "Jumex Lata",
                        "https://drive.google.com/thumbnail?id=1UlTILnkyeo25amhkSwTfDGAa4tteEGeg",
                        "Jugo en lata 355ml",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()),
                new ProductEntity(
                        14L,
                        bebidasCategory,
                        "Agua de frutos",
                        "https://drive.google.com/thumbnail?id=1c8g7ud-wX-As_8f5Mxz2m7xv1XjFUXuv",
                        "Vaso de agua natural",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        15L,
                        botanasCategory,
                        "Tostitos con queso",
                        "https://drive.google.com/thumbnail?id=1DIsR7cDco6oOlL3YVngzRWkD9HnTH6yt",
                        "Tostitos con queso amarillo y salsa a elegir",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                ),
                new ProductEntity(
                        16L,
                        botanasCategory,
                        "Doritos con queso",
                        "https://drive.google.com/thumbnail?id=1rmm4ahwUIf8Nixb3hRzkeuCI3mpfVTOk",
                        "Doritos con queso amarillo y salsa a elegir",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                )

        );
        productRepository.saveAll(products);
    }

    private void initializeProductFlavors() {
        // Primero obtenemos las categorías necesarias
        ProductEntity doriesquites = productRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Producto Doriesquites no encontrado"));
        ProductEntity tostiesquites = productRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Producto Tostiesquites no encontrado"));
        ProductEntity esquitesConMaruchan = productRepository.findById(5L)
                .orElseThrow(() -> new IllegalStateException("Producto Esquites Con Queso no encontrado"));
        ProductEntity eloteRevolcado = productRepository.findById(9L)
                .orElseThrow(() -> new IllegalStateException("Producto Coca cola no encontrado"));
        ProductEntity boing = productRepository.findById(12L)
                .orElseThrow(() -> new IllegalStateException("Producto Boing no encontrado"));
        ProductEntity jumex = productRepository.findById(13L)
                .orElseThrow(() -> new IllegalStateException("Producto Jumex no encontrado"));
        ProductEntity agua = productRepository.findById(14L)
                .orElseThrow(() -> new IllegalStateException("Producto Agua no encontrado"));

        List<ProductFlavorEntity> flavors = Arrays.asList(
                new ProductFlavorEntity(
                        1L,
                        "Nachos",
                        doriesquites
                ),
                new ProductFlavorEntity(
                        2L,
                        "Incognita",
                        doriesquites
                ),
                new ProductFlavorEntity(
                        3L,
                        "Flaming Hot",
                        doriesquites
                ),
                new ProductFlavorEntity(
                        4L,
                        "Salsa Verde",
                        tostiesquites
                ),
                new ProductFlavorEntity(
                        5L,
                        "Flaming Hot",
                        tostiesquites
                ),
                new ProductFlavorEntity(
                        6L,
                        "Camaron",
                        esquitesConMaruchan
                ),
                new ProductFlavorEntity(
                        7L,
                        "Camaron Habanero",
                        esquitesConMaruchan
                ),
                new ProductFlavorEntity(
                        8L,
                        "Camaron Piquin",
                        esquitesConMaruchan
                ),
                new ProductFlavorEntity(
                        9L,
                        "Pollo",
                        esquitesConMaruchan
                ),
                new ProductFlavorEntity(
                        10L,
                        "Res",
                        esquitesConMaruchan
                ),
                new ProductFlavorEntity(
                        11L,
                        "Sabritas Cruji FH",
                        eloteRevolcado
                ),
                new ProductFlavorEntity(
                        12L,
                        "Rancheritos",
                        eloteRevolcado
                ),
                new ProductFlavorEntity(
                        13L,
                        "Doritos Nachos",
                        eloteRevolcado
                ),
                new ProductFlavorEntity(
                        14L,
                        "Mango",
                        boing
                ),
                new ProductFlavorEntity(
                        15L,
                        "Manzana",
                        boing
                ),
                new ProductFlavorEntity(
                        16L,
                        "Guayaba",
                        boing
                ),
                new ProductFlavorEntity(
                        17L,
                        "Mango",
                        jumex
                ),
                new ProductFlavorEntity(
                        18L,
                        "Durazno",
                        jumex
                ),
                new ProductFlavorEntity(
                        19L,
                        "Manzana",
                        jumex
                ),
                new ProductFlavorEntity(
                        20L,
                        "Jamaica",
                        agua
                ),
                new ProductFlavorEntity(
                        21L,
                        "Limón",
                        agua
                ),
                new ProductFlavorEntity(
                        22L,
                        "Limón con chia",
                        agua
                ),
                new ProductFlavorEntity(
                        23L,
                        "Frutos Rojos",
                        agua
                ),
                new ProductFlavorEntity(
                        24L,
                        "Mango",
                        agua
                )
        );
        productFlavorRepository.saveAll(flavors);
    }

    private void initializeProductVariants() {
        // Obtenemos los productos necesarios
        ProductEntity doriesquites = productRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Producto Doriesquites no encontrado"));
        ProductEntity tostiesquites = productRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Producto Tostiesquites no encontrado"));
        ProductEntity esquitesTrad = productRepository.findById(3L)
                .orElseThrow(() -> new IllegalStateException("Producto Esquites Tradicionales no encontrado"));
        ProductEntity esquitesConQueso = productRepository.findById(4L)
                .orElseThrow(() -> new IllegalStateException("Producto Esquites Con Queso no encontrado"));
        ProductEntity esquitesConMaruchan = productRepository.findById(5L)
                .orElseThrow(() -> new IllegalStateException("Producto Esquites Con Queso no encontrado"));
        ProductEntity maizPuerco = productRepository.findById(6L)
                .orElseThrow(() -> new IllegalStateException("Producto Maiz Puerco no encontrado"));
        ProductEntity maizPuercoSinSopa = productRepository.findById(7L)
                .orElseThrow(() -> new IllegalStateException("Producto Maiz Puerco sin sopa no encontrado"));
        ProductEntity eloteTradicional = productRepository.findById(8L)
                .orElseThrow(() -> new IllegalStateException("Producto Elote Tradicional no encontrado"));
        ProductEntity eloteRevolcado = productRepository.findById(9L)
                .orElseThrow(() -> new IllegalStateException("Producto Elote Revolcado no encontrado"));
        ProductEntity redCola = productRepository.findById(10L)
                .orElseThrow(() -> new IllegalStateException("Producto Red cola no encontrado"));
        ProductEntity cocaCola = productRepository.findById(11L)
                .orElseThrow(() -> new IllegalStateException("Producto Coca cola no encontrado"));
        ProductEntity boing = productRepository.findById(12L)
                .orElseThrow(() -> new IllegalStateException("Producto Boing no encontrado"));
        ProductEntity jumex = productRepository.findById(13L)
                .orElseThrow(() -> new IllegalStateException("Producto Jumex no encontrado"));
        ProductEntity agua = productRepository.findById(14L)
                .orElseThrow(() -> new IllegalStateException("Producto Agua no encontrado"));
        ProductEntity tostiQueso = productRepository.findById(15L)
                .orElseThrow(() -> new IllegalStateException("Producto TostiQueso no encontrado"));
        ProductEntity doriQueso = productRepository.findById(16L)
                .orElseThrow(() -> new IllegalStateException("Producto DoriQueso no encontrado"));

        List<ProductVariantEntity> variants = Arrays.asList(
                //Maiz puerco
                new ProductVariantEntity(1L, maizPuerco, "Único", BigDecimal.valueOf(120), BigDecimal.valueOf(80), LocalDateTime.now()),
                new ProductVariantEntity(2L, maizPuercoSinSopa, "Único", BigDecimal.valueOf(100), BigDecimal.valueOf(60), LocalDateTime.now()),

                //Red cola
                new ProductVariantEntity(3L, redCola, "600ml", BigDecimal.valueOf(20), BigDecimal.valueOf(12.31), LocalDateTime.now()),

                //Boing
                new ProductVariantEntity(4L, boing, "500ml", BigDecimal.valueOf(20), BigDecimal.valueOf(12.55), LocalDateTime.now()),

                //Jumex
                new ProductVariantEntity(5L, jumex, "355ml", BigDecimal.valueOf(20), BigDecimal.valueOf(11), LocalDateTime.now()),

                //Esquites Con Maruchan
                new ProductVariantEntity(6L, esquitesConMaruchan, "Único", BigDecimal.valueOf(70), BigDecimal.valueOf(32), LocalDateTime.now()),

                //Elotes
                new ProductVariantEntity(7L, eloteTradicional, "Único", BigDecimal.valueOf(30), BigDecimal.valueOf(12), LocalDateTime.now()),
                new ProductVariantEntity(8L, eloteRevolcado, "Único", BigDecimal.valueOf(45), BigDecimal.valueOf(25), LocalDateTime.now()),

                // Esquites Tradicionales
                new ProductVariantEntity(9L, esquitesTrad, "Chico (8oz)", BigDecimal.valueOf(30.00), BigDecimal.valueOf(10.00), LocalDateTime.now()),
                new ProductVariantEntity(10L, esquitesTrad, "Mediano (10oz)", BigDecimal.valueOf(40.00), BigDecimal.valueOf(15.00), LocalDateTime.now()),
                new ProductVariantEntity(11L, esquitesTrad, "Grande (12oz)", BigDecimal.valueOf(50.00), BigDecimal.valueOf(20.00), LocalDateTime.now()),
                new ProductVariantEntity(12L, esquitesTrad, "Extra Grande (14oz)", BigDecimal.valueOf(60.00), BigDecimal.valueOf(25.00), LocalDateTime.now()),

                // Doriesquites
                new ProductVariantEntity(13L, doriesquites, "Único", BigDecimal.valueOf(55.00), BigDecimal.valueOf(20.00), LocalDateTime.now()),

                //Coca
                new ProductVariantEntity(14L, cocaCola, "400ml", BigDecimal.valueOf(20), BigDecimal.valueOf(13.42), LocalDateTime.now()),
                new ProductVariantEntity(15L, cocaCola, "600ml", BigDecimal.valueOf(25), BigDecimal.valueOf(17.9), LocalDateTime.now()),
                new ProductVariantEntity(16L, cocaCola, "355ml", BigDecimal.valueOf(25), BigDecimal.valueOf(17.9), LocalDateTime.now()),

                //Aguas frescas
                new ProductVariantEntity(17L, agua, "500ml", BigDecimal.valueOf(25), BigDecimal.valueOf(15), LocalDateTime.now()),
                new ProductVariantEntity(18L, agua, "1000ml", BigDecimal.valueOf(45), BigDecimal.valueOf(25), LocalDateTime.now()),

                //Esquites con queso
                new ProductVariantEntity(19L, esquitesConQueso, "Chico (8oz)", BigDecimal.valueOf(35), BigDecimal.valueOf(15), LocalDateTime.now()),
                new ProductVariantEntity(20L, esquitesConQueso, "Mediano (10oz)", BigDecimal.valueOf(45), BigDecimal.valueOf(20), LocalDateTime.now()),
                new ProductVariantEntity(21L, esquitesConQueso, "Grande (12oz)", BigDecimal.valueOf(55), BigDecimal.valueOf(25), LocalDateTime.now()),
                new ProductVariantEntity(22L, esquitesConQueso, "Extra Grande (14oz)", BigDecimal.valueOf(65), BigDecimal.valueOf(30), LocalDateTime.now()),
                new ProductVariantEntity(23L, tostiesquites, "Único", BigDecimal.valueOf(55.00), BigDecimal.valueOf(30), LocalDateTime.now()),
                new ProductVariantEntity(24L, tostiQueso, "Único", BigDecimal.valueOf(25.00), BigDecimal.valueOf(17), LocalDateTime.now()),
                new ProductVariantEntity(25L, doriQueso, "Único", BigDecimal.valueOf(25.00), BigDecimal.valueOf(17), LocalDateTime.now())

        );

        productVariantRepository.saveAll(variants);

    }
}