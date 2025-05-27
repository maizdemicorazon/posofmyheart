package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Obtener menú completo",
            description = "Devuelve todos los productos, extras disponibles y detalles disponibles"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Menú obtenido exitosamente"),
                    @ApiResponse(responseCode = "404", description = "No hay productos disponibles en el menú"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @Cacheable("menu")
    @GetMapping
    public ResponseEntity<ProductsWithExtrasDto> getMenu() {
        return ResponseEntity.ok(productService.getMenuProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
