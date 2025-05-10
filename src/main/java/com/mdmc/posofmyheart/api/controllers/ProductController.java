package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.models.OrderRequest;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping
    public List<Product> getMenu() {
        return productService.getMenuProducts();
    }

    @PostMapping("/order")
    public ResponseEntity<Integer> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderEntity order = orderService.createOrder(request);
        return ResponseEntity.ok(order.getId());
    }


}