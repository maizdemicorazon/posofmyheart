package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.api.exceptions.ResourceNotFoundException;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductsWithExtrasDto> getMenuProducts() {
        return Optional.ofNullable(productService.getMenuProducts())
                .map(products -> {
                    if (products.products() == null || products.products().isEmpty()) {
                        throw new ResourceNotFoundException("Productos no encontrados");
                    }
                    return ResponseEntity.ok(products);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Productos no encontrados"));
    }


}