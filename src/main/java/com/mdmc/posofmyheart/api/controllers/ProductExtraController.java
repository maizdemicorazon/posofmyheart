package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.services.ProductExtraService;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/extras")
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
