package com.mdmc.posofmyheart.api.controllers;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.application.services.ProductFlavorService;
import com.mdmc.posofmyheart.domain.models.ProductFlavor;

@RestController
@RequestMapping("/products/{productId}/flavors")
@RequiredArgsConstructor
@Log4j2
public class ProductFlavorRelationController {

    private final ProductFlavorService productFlavorService;

    @Operation(
            summary = "Obtener sabores de un producto",
            description = "Devuelve todos los sabores disponibles para un producto específico"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Sabores obtenidos exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @Cacheable(value = "flavors", key = "'product_' + #productId")
    @GetMapping
    public ResponseEntity<List<ProductFlavor>> getFlavorsByProduct(@PathVariable Long productId) {
        log.info("Getting flavors for product: {}", productId);
        List<ProductFlavor> flavors = productFlavorService.getFlavorsByProductId(productId);
        log.info("Found {} flavors for product: {}", flavors.size(), productId);
        return ResponseEntity.ok(flavors);
    }

    @Operation(
            summary = "Obtener sabores activos de un producto",
            description = "Devuelve solo los sabores activos para un producto específico"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Sabores activos obtenidos exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @Cacheable(value = "flavors", key = "'active_product_' + #productId")
    @GetMapping("/active")
    public ResponseEntity<List<ProductFlavor>> getActiveFlavorsByProduct(@PathVariable Long productId) {
        log.info("Getting active flavors for product: {}", productId);
        List<ProductFlavor> flavors = productFlavorService.getActiveFlavorsByProductId(productId);
        log.info("Found {} active flavors for product: {}", flavors.size(), productId);
        return ResponseEntity.ok(flavors);
    }

    @Operation(
            summary = "Verificar disponibilidad de sabor",
            description = "Verifica si un sabor específico está disponible para un producto"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Verificación completada"),
                    @ApiResponse(responseCode = "404", description = "Producto o sabor no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{flavorId}/availability")
    public ResponseEntity<FlavorAvailabilityResponse> checkFlavorAvailability(
            @PathVariable Long productId,
            @PathVariable Long flavorId) {

        log.info("Checking availability of flavor {} for product {}", flavorId, productId);
        boolean isAvailable = productFlavorService.isFlavorAvailableForProduct(productId, flavorId);

        FlavorAvailabilityResponse response = new FlavorAvailabilityResponse(
                productId,
                flavorId,
                isAvailable
        );

        log.info("Flavor {} availability for product {}: {}", flavorId, productId, isAvailable);
        return ResponseEntity.ok(response);
    }

    public record FlavorAvailabilityResponse(
            Long productId,
            Long flavorId,
            boolean available
    ) {
    }
}