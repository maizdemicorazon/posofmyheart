package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.services.impl.OrderServiceImpl;
import com.mdmc.posofmyheart.domain.models.OrderItemRequest;
import com.mdmc.posofmyheart.domain.models.OrderRequest;
import com.mdmc.posofmyheart.domain.models.Price;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductPriceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private ProductEntity testProduct;
    private PaymentMethodEntity testPaymentMethod;

    @BeforeEach
    void setUp() {

        ArrayList<ProductPriceEntity> prices = new ArrayList<>();
        ProductPriceEntity productPriceEntity = new ProductPriceEntity();
        productPriceEntity.setId(101);
        productPriceEntity.setSellPrice(BigDecimal.valueOf(50.00));
        productPriceEntity.setCostPrice(BigDecimal.valueOf(30.00));

        prices.add(productPriceEntity);

        testProduct = new ProductEntity();
        testProduct.setId(101);
        testProduct.setPrices(prices);

        testPaymentMethod = new PaymentMethodEntity();
        testPaymentMethod.setId(1);
        testPaymentMethod.setName("Tarjeta de Crédito");
    }

    @Test
    void createOrder_ShouldNotThrowNPE_WhenDetailsListIsInitialized() {
        // Configuración de mocks
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(testPaymentMethod));
        when(productRepository.findById(101L)).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity savedOrder = invocation.getArgument(0);
            savedOrder.setId(1000); // Simula ID generado
            return savedOrder;
        });

        // Datos de prueba
        OrderRequest request = new OrderRequest(
                1L,
                "Sin cebolla",
                List.of(new OrderItemRequest(101L, 2))
        );

        // Ejecución y verificación
        assertDoesNotThrow(() -> {
            Integer orderId = orderService.createOrder(request).getId();
            assertEquals(1000, orderId);
        });

        // Verifica que se llamó a save()
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void createOrder_ShouldCalculateCorrectTotal() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(testPaymentMethod));
        when(productRepository.findById(101L)).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation ->
                invocation.getArgument(0));

        OrderRequest request = new OrderRequest(
                1L,
                null,
                List.of(
                        new OrderItemRequest(101L, 2), // 50.00 x 2 = 100.00
                        new OrderItemRequest(101L, 1)  // 50.00 x 1 = 50.00
                )
        );

        OrderEntity result = orderService.createOrder(request);

        assertEquals(0, BigDecimal.valueOf(150.00).compareTo(result.getTotalAmount()),
                "El total debe ser 150.00");
    }

    @Test
    void createOrder_ShouldThrow_WhenProductNotFound() {
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(testPaymentMethod));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        OrderRequest request = new OrderRequest(
                1L,
                null,
                List.of(new OrderItemRequest(999L, 1))
        );

        assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(request));
    }
}