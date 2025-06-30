package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.services.ProductVariantService;
import com.mdmc.posofmyheart.domain.models.ProductVariant;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService variantService;

    @Cacheable("variants")
    @GetMapping
    public ResponseEntity<List<ProductVariant>> getAllVariants() {
        return ResponseEntity.ok(variantService.getAllVariants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getVariantById(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.getVariantById(id));
    }

}