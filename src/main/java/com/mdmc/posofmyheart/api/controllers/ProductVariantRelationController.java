package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.services.ProductVariantService;
import com.mdmc.posofmyheart.domain.models.ProductVariant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products/{productId}/variants")
@RequiredArgsConstructor
@Log4j2
@Validated
public class ProductVariantRelationController {

    private final ProductVariantService productVariantService;

    @Operation(
            summary = "Obtener variantes de un producto",
            description = "Devuelve todas las variantes disponibles para un producto específico"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Variantes obtenidas exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @Cacheable(value = "variants", key = "'product_' + #productId")
    @GetMapping
    public ResponseEntity<List<ProductVariant>> getVariantsByProduct(@PathVariable Long productId) {
        log.info("Getting variants for product: {}", productId);
        List<ProductVariant> variants = productVariantService.getVariantsByProductId(productId);
        log.info("Found {} variants for product: {}", variants.size(), productId);
        return ResponseEntity.ok(variants);
    }

    @Operation(
            summary = "Obtener variantes activas de un producto",
            description = "Devuelve solo las variantes activas (con precio) para un producto específico"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Variantes activas obtenidas exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @Cacheable(value = "variants", key = "'active_product_' + #productId")
    @GetMapping("/active")
    public ResponseEntity<List<ProductVariant>> getActiveVariantsByProduct(@PathVariable Long productId) {
        log.info("Getting active variants for product: {}", productId);
        List<ProductVariant> variants = productVariantService.getActiveVariantsByProductId(productId);
        log.info("Found {} active variants for product: {}", variants.size(), productId);
        return ResponseEntity.ok(variants);
    }

    @Operation(
            summary = "Verificar disponibilidad de variante",
            description = "Verifica si una variante específica está disponible para un producto"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Verificación completada"),
                    @ApiResponse(responseCode = "404", description = "Producto o variante no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{variantId}/availability")
    public ResponseEntity<VariantAvailabilityResponse> checkVariantAvailability(
            @PathVariable Long productId,
            @PathVariable Long variantId) {

        log.info("Checking availability of variant {} for product {}", variantId, productId);
        boolean isAvailable = productVariantService.isVariantAvailableForProduct(productId, variantId);

        VariantAvailabilityResponse response = new VariantAvailabilityResponse(
                productId,
                variantId,
                isAvailable
        );

        log.info("Variant {} availability for product {}: {}", variantId, productId, isAvailable);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar variantes por rango de precio",
            description = "Devuelve las variantes de un producto dentro de un rango de precios específico"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Variantes encontradas exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Rango de precios inválido"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductVariant>> getVariantsByPriceRange(
            @PathVariable Long productId,
            @RequestParam @DecimalMin(value = "0.0", message = "Min price must be positive") BigDecimal minPrice,
            @RequestParam @DecimalMin(value = "0.0", message = "Max price must be positive") BigDecimal maxPrice) {

        log.info("Getting variants for product {} with price range: {} - {}", productId, minPrice, maxPrice);
        List<ProductVariant> variants = productVariantService.getVariantsByProductIdAndPriceRange(productId, minPrice, maxPrice);
        log.info("Found {} variants in price range for product: {}", variants.size(), productId);
        return ResponseEntity.ok(variants);
    }

    @Operation(
            summary = "Obtener variante más barata",
            description = "Devuelve la variante con el precio más bajo para un producto"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Variante más barata encontrada"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado o sin variantes"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/cheapest")
    public ResponseEntity<ProductVariant> getCheapestVariant(@PathVariable Long productId) {
        log.info("Getting cheapest variant for product: {}", productId);
        Optional<ProductVariant> variant = productVariantService.getCheapestVariantByProductId(productId);

        return variant.map(v -> {
            log.info("Found cheapest variant {} for product: {}", v.idVariant(), productId);
            return ResponseEntity.ok(v);
        }).orElseGet(() -> {
            log.info("No variants found for product: {}", productId);
            return ResponseEntity.notFound().build();
        });
    }

    @Operation(
            summary = "Obtener variante más cara",
            description = "Devuelve la variante con el precio más alto para un producto"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Variante más cara encontrada"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado o sin variantes"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/most-expensive")
    public ResponseEntity<ProductVariant> getMostExpensiveVariant(@PathVariable Long productId) {
        log.info("Getting most expensive variant for product: {}", productId);
        Optional<ProductVariant> variant = productVariantService.getMostExpensiveVariantByProductId(productId);

        return variant.map(v -> {
            log.info("Found most expensive variant {} for product: {}", v.idVariant(), productId);
            return ResponseEntity.ok(v);
        }).orElseGet(() -> {
            log.info("No variants found for product: {}", productId);
            return ResponseEntity.notFound().build();
        });
    }

    @Operation(
            summary = "Buscar variante por tamaño",
            description = "Devuelve la variante específica de un producto por su tamaño"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Variante encontrada"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado o variante no existe"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/size/{size}")
    public ResponseEntity<ProductVariant> getVariantBySize(
            @PathVariable Long productId,
            @PathVariable @NotBlank(message = "Size cannot be blank") String size) {

        log.info("Getting variant for product {} with size: {}", productId, size);
        Optional<ProductVariant> variant = productVariantService.getVariantByProductIdAndSize(productId, size);

        return variant.map(v -> {
            log.info("Found variant {} for product {} with size: {}", v.idVariant(), productId, size);
            return ResponseEntity.ok(v);
        }).orElseGet(() -> {
            log.info("No variant found for product {} with size: {}", productId, size);
            return ResponseEntity.notFound().build();
        });
    }

    @Operation(
            summary = "Obtener tamaños disponibles",
            description = "Devuelve todos los tamaños disponibles para un producto"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Tamaños obtenidos exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/sizes")
    public ResponseEntity<AvailableSizesResponse> getAvailableSizes(@PathVariable Long productId) {
        log.info("Getting available sizes for product: {}", productId);
        List<String> sizes = productVariantService.getAvailableSizesByProductId(productId);

        AvailableSizesResponse response = new AvailableSizesResponse(productId, sizes);
        log.info("Found {} sizes for product: {}", sizes.size(), productId);
        return ResponseEntity.ok(response);
    }

    // Records para las respuestas
    public record VariantAvailabilityResponse(
            Long productId,
            Long variantId,
            boolean available
    ) {}

    public record AvailableSizesResponse(
            Long productId,
            List<String> sizes
    ) {}
}