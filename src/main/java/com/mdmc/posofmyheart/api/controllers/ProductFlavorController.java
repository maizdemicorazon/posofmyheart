package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.services.ProductFlavorService;
import com.mdmc.posofmyheart.domain.models.ProductFlavor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flavors")
@RequiredArgsConstructor
public class ProductFlavorController {

    private final ProductFlavorService productFlavorService;

    @Cacheable("flavors")
    @GetMapping
    public List<ProductFlavor> getAllFlavors() {
        return productFlavorService.getAllFlavors();
    }

    @GetMapping("/{id}")
    public ProductFlavor getFlavorById(@PathVariable Long id) {
        return productFlavorService.getFlavorById(id);
    }

}