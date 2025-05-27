package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.api.exceptions.GlobalExceptionHandler;
import com.mdmc.posofmyheart.api.exceptions.MenuNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.domain.models.Sauce;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getMenu_ShouldReturnMenuWithProductsExtrasAndSauces() throws Exception {
        // Arrange
        ProductVariant variant = new ProductVariant("Grande", new BigDecimal("12.99"));
        Product product = new Product(1L, 1L, "Pizza Margarita", "pizza.jpg", List.of(variant));
        ProductExtra extra = new ProductExtra(1L, "Queso extra", new BigDecimal("2.99"));
        Sauce sauce = new Sauce(1L, "Barbacoa");

        ProductsWithExtrasDto expectedDto = new ProductsWithExtrasDto(
                List.of(product),
                List.of(extra),
                List.of(sauce)
        );

        when(productService.getMenuProducts()).thenReturn(expectedDto);

        // Act & Assert
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].idProduct").value(1L))
                .andExpect(jsonPath("$.extras[0].idExtra").value(1L))
                .andExpect(jsonPath("$.sauces[0].idSauce").value(1L));

        verify(productService, times(1)).getMenuProducts();
    }

    @Test
    void getMenu_ShouldReturn404WhenNoProductsAvailable() throws Exception {
        // Arrange
        when(productService.getMenuProducts()).thenThrow(new MenuNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No hay productos disponibles en el menú"));
    }

    @Test
    void getMenu_ShouldReturn500WhenUnexpectedErrorOccurs() throws Exception {
        // Arrange
        when(productService.getMenuProducts()).thenThrow(new RuntimeException("Error inesperado"));

        // Act & Assert
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Ocurrió un error inesperado"));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        // Arrange
        ProductVariant variant = new ProductVariant("Grande", new BigDecimal("12.99"));
        Product expectedProduct = new Product(1L, 1L, "Pizza Margarita", "pizza.jpg", List.of(variant));

        when(productService.getProductById(1L)).thenReturn(expectedProduct);

        // Act & Assert
        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduct").value(1L))
                .andExpect(jsonPath("$.name").value("Pizza Margarita"));
    }

    @Test
    void getProductById_ShouldReturn404WhenProductNotFound() throws Exception {
        // Arrange
        when(productService.getProductById(99L)).thenThrow(new ProductNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));
    }

    @Test
    void getProductById_ShouldReturn400WhenInvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
