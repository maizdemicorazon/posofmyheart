package com.mdmc.posofmyheart.api.controllers;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.mdmc.posofmyheart.application.services.ProductExtraService;
import com.mdmc.posofmyheart.domain.models.ProductExtra;

@RestController
@RequestMapping("/extras")
@RequiredArgsConstructor
public class ProductExtraController {

    private final ProductExtraService productExtraService;

    @GetMapping("/{id}")
    public ProductExtra getExtraById(@PathVariable Long id) {
        return productExtraService.getExtraById(id);
    }

    @Cacheable("extras")
    @GetMapping
    public List<ProductExtra> getAllExtras() {
        return productExtraService.getAllExtras();
    }

}
