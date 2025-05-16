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
    private final SauceRepository sauceRepository;
    private final ProductRepository productRepository;
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
        if (sauceRepository.count() == 0) {
            initializeSauces();
        }
        if (productExtraRepository.count() == 0) {
            initializeProductExtras();
        }
        if (productRepository.count() == 0) {
            initializeProducts();
        }
        if (productVariantRepository.count() == 0) {
            initializeProductVariants();
        }

        log.info("Inicialización de datos completada.");
    }

    private void initializePaymentMethods() {
        List<PaymentMethodEntity> methods = Arrays.asList(
                new PaymentMethodEntity(1L, "Efectivo", "Pago en efectivo", true, LocalDateTime.now()),
                new PaymentMethodEntity(2L, "Tarjeta de crédito", "Pago con tarjeta de crédito", true, LocalDateTime.now()),
                new PaymentMethodEntity(3L, "Tarjeta de débito", "Pago con tarjeta de débito", true, LocalDateTime.now()),
                new PaymentMethodEntity(4L, "Transferencia", "Pago por transferencia bancaria", true, LocalDateTime.now()),
                new PaymentMethodEntity(5L, "QR", "Pago a través de Mercado Pago", true, LocalDateTime.now())
        );
        paymentMethodRepository.saveAll(methods);
    }

    private void initializeProductCategories() {

        List<ProductCategoryEntity> categories = Arrays.asList(
                new ProductCategoryEntity(1L, "Esquites", "Variedad de esquites tradicionales y especiales"),
                new ProductCategoryEntity(2L, "Elotes", "Elotes preparados de diferentes formas"),
                new ProductCategoryEntity(3L, "Bebidas", "Refrescos, jugos y aguas")
        );
        productCategoryRepository.saveAll(categories);
        log.info("Categorías de productos inicializadas");

    }

    private void initializeSauces() {
        List<SauceEntity> sauces = Arrays.asList(
                new SauceEntity(1L, "Tradicional", "Mayonesa, queso cotija y chile piquín"),
                new SauceEntity(2L, "Valentina", "Salsa Valentina clásica"),
                new SauceEntity(3L, "Habanero", "Salsa picante de habanero"),
                new SauceEntity(4L, "Chipotle", "Salsa ahumada de chipotle"),
                new SauceEntity(5L, "Sin picante", "Sin salsa picante"),
                new SauceEntity(6L, "Mixta", "Combinación de salsas al gusto"),
                new SauceEntity(7L, "Macha", "Salsa de la casa")
        );
        sauceRepository.saveAll(sauces);
    }

    private void initializeProductExtras() {
        List<ProductExtraEntity> extras = Arrays.asList(
                new ProductExtraEntity(1L,
                        "Queso Manchego Extra",
                        "Rebanada de queso extra",
                        BigDecimal.valueOf(7.00),
                        BigDecimal.valueOf(3.50),
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                ),
                new ProductExtraEntity(2L,
                        "Queso Amarillo Extra",
                        "50 ml Queso amarillo liquido extra",
                        BigDecimal.valueOf(4.00),
                        BigDecimal.valueOf(2.00),
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                ),
                new ProductExtraEntity(3L,
                        "Costilla extra",
                        "200g de costilla de puerco extra",
                        BigDecimal.valueOf(20.00),
                        BigDecimal.valueOf(10.00),
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                ),
                new ProductExtraEntity(4L,
                        "Porción de Falda Extra",
                        "200g de falda de res deshebrada extra",
                        BigDecimal.valueOf(22.00),
                        BigDecimal.valueOf(11.00),
                        true,
                        "",
                        LocalDateTime.now(),
                        List.of()
                ),
                new ProductExtraEntity(5L,
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

        List<ProductEntity> products = Arrays.asList(
                new ProductEntity(
                        1L,
                        esquitesCategory,
                        "Doriesquites",
                        "https://drive.google.com/thumbnail?id=1GmaqB_ifP0h-Ck6S_ENZi45PfH7tbwEq",
                        "Doritos con esquites",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        2L,
                        esquitesCategory,
                        "Tostiesquites",
                        "https://drive.google.com/thumbnail?id=1v2OT5_GYNYSsr7XaHqnsxEEax1jLtWit",
                        "Tostiesquites con esquites",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        3L,
                        esquitesCategory,
                        "Esquites Tradicionales",
                        "https://drive.google.com/thumbnail?id=1ph3Iog_GrUF6VY2DmiapwquOCUcTDTNx",
                        "Esquites con mayonesa, queso y picante",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        4L,
                        esquitesCategory,
                        "Esquites con Queso Manchego/Amarillo",
                        "https://drive.google.com/thumbnail?id=1ZG_sIFvIvUyBNK0x1as1WvDNAC2RIP10",
                        "Esquites tradicionales con queso extra",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        5L,
                        esquitesCategory,
                        "Esquite Maruchan",
                        "https://drive.google.com/thumbnail?id=1xOzM-Yl3VVgGufYwaxmIpDHQ94yEzEIY",
                        "Sopa maruchan con esquites",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        6L,
                        esquitesCategory,
                        "Maíz Puerco",
                        "https://drive.google.com/thumbnail?id=1_DwAv9akn8dmrq-sO_PAAaAEEGPwPIF_",
                        "Maruchan con esquites y carne de puerco",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        7L,
                        esquitesCategory,
                        "Maíz Puerco sin sopa",
                        "https://drive.google.com/thumbnail?id=1_DwAv9akn8dmrq-sO_PAAaAEEGPwPIF_",
                        "Esquites con carne de puerco sin maruchan",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        8L,
                        elotesCategory,
                        "Elote Tradicional",
                        "https://drive.google.com/thumbnail?id=1R0z_q1JH1H7mlpSvbUt36pGbfGuUPmJk",
                        "Elote con mayonesa, queso y picante",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        9L,
                        elotesCategory,
                        "Elote Revolcado",
                        "https://drive.google.com/thumbnail?id=1i1r2iQQIfm0TsfbCJxxlEGahcmT5TwWc",
                        "Elote con topping a elegir",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        10L,
                        bebidasCategory,
                        "Red Cola 600ml",
                        "Refresco en botella PET",
                        "https://drive.google.com/thumbnail?id=1bDM3IJmOQDtl1xz1OquqH4UFiKU90J5d",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        11L,
                        bebidasCategory,
                        "Coca Cola",
                        "https://drive.google.com/thumbnail?id=1C41w2JUpvJleSgnrUiLR5s4jXek0bMUM",
                        "Refresco en botella PET o lata",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        12L,
                        bebidasCategory,
                        "Boing 500ml",
                        "https://drive.google.com/thumbnail?id=1PUk_tLBQoctGdNcG1MsbrGHEE6SONwTd",
                        "Jugo en caja 500ml",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        13L,
                        bebidasCategory,
                        "Jumex Lata",
                        "https://drive.google.com/thumbnail?id=1UlTILnkyeo25amhkSwTfDGAa4tteEGeg",
                        "Jugo en lata 355ml",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()),
                new ProductEntity(
                        14L,
                        bebidasCategory,
                        "Agua de frutos",
                        "https://drive.google.com/thumbnail?id=1c8g7ud-wX-As_8f5Mxz2m7xv1XjFUXuv",
                        "Vaso de agua natural",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of())
        );
        productRepository.saveAll(products);
    }

    private void initializeProductVariants() {
        // Obtenemos los productos necesarios
        ProductEntity doriesquites = productRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Producto Doriesquites no encontrado"));
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
                new ProductVariantEntity(13L, doriesquites, "Regular", BigDecimal.valueOf(55.00), BigDecimal.valueOf(20.00), LocalDateTime.now()),

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
                new ProductVariantEntity(22L, esquitesConQueso, "Extra Grande (14oz)", BigDecimal.valueOf(65), BigDecimal.valueOf(30), LocalDateTime.now())
        );

        productVariantRepository.saveAll(variants);

    }
}