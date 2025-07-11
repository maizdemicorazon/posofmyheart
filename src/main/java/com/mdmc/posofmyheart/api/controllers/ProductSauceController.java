package com.mdmc.posofmyheart.api.controllers;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.mdmc.posofmyheart.application.services.ProductSauceService;
import com.mdmc.posofmyheart.domain.models.ProductSauce;

@RestController
@RequestMapping("/sauces")
@RequiredArgsConstructor
public class ProductSauceController {

    private final ProductSauceService productSauceService;

    @Cacheable("sauces")
    @GetMapping
    public List<ProductSauce> getAllFlavors() {
        return productSauceService.getAllSauces();
    }

    @GetMapping("/{id}")
    public ProductSauce getFlavorById(@PathVariable Long id) {
        return productSauceService.getSauceById(id);
    }

}
