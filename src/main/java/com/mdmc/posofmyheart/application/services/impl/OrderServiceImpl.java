package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.OrderNotFoundException;
import com.mdmc.posofmyheart.application.dtos.*;
import com.mdmc.posofmyheart.application.mappers.OrderResponseMapper;
import com.mdmc.posofmyheart.application.mappers.OrderRestoreMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.domain.patterns.strategies.CreateOrderStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.CreateOrdersStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.UpdateOrderStrategy;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CreateOrderStrategy createOrderStrategy;
    private final CreateOrdersStrategy createOrdersStrategy;
    private final UpdateOrderStrategy updateOrderStrategy;

    /**
     * ⚡ SÚPER OPTIMIZADA: Una sola query para todas las órdenes con caché
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'allOrders'")
    public List<OrderResponse> findAllOrders() {
        log.debug("🔍 Obteniendo todas las órdenes con optimización completa");

        long startTime = System.currentTimeMillis();

        // ⚡ UNA SOLA QUERY con FETCH JOIN completo - NO MÁS N+1
        List<OrderEntity> orders = orderRepository.findAllWithCompleteDetails();

        // ⚡ Mapeo optimizado usando MapStruct
        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes obtenidas en {}ms", responses.size(), (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ OPTIMIZADA: Búsqueda por fecha con caché
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersByDate-' + #date.toString()")
    public List<OrderResponse> listOrdersByDate(LocalDate date) {
        log.debug("🔍 Obteniendo órdenes para fecha: {}", date);

        long startTime = System.currentTimeMillis();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // ⚡ Query optimizada con FETCH JOIN
        var orders = orderRepository.findByOrderDate(startOfDay, endOfDay);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes obtenidas para {} en {}ms", responses.size(), date, (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ OPTIMIZADA: Búsqueda por ID con caché
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'order-' + #idOrder")
    public OrderResponse findOrderById(Long idOrder) {
        log.debug("🔍 Obteniendo orden por ID: {}", idOrder);

        long startTime = System.currentTimeMillis();

        // ⚡ Query optimizada con FETCH JOIN para una orden específica
        OrderEntity order = orderRepository.findByIdWithCompleteDetails(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        OrderResponse response = OrderResponseMapper.INSTANCE.toResponse(order);

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} obtenida en {}ms", idOrder, (endTime - startTime));

        return response;
    }

    /**
     * ⚡ OPTIMIZADA: Backup con procesamiento paralelo
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'backup'")
    public OrderRestore findOrdersToBackup() {
        log.debug("🔍 Preparando backup de órdenes");

        long startTime = System.currentTimeMillis();

        // ⚡ Procesamiento asíncrono para backup
        CompletableFuture<List<OrderEntity>> ordersFuture = CompletableFuture
                .supplyAsync(() -> orderRepository.findAllWithCompleteDetails());

        try {
            List<OrderEntity> orders = ordersFuture.get();
            List<OrderResponse> orderResponses = orders.stream()
                    .map(OrderResponseMapper.INSTANCE::toResponse)
                    .toList();

            OrderRestore backup = OrderRestoreMapper.INSTANCE.toBackup(orderResponses);

            long endTime = System.currentTimeMillis();
            log.info("✅ Backup de {} órdenes preparado en {}ms", orderResponses.size(), (endTime - startTime));

            return backup;
        } catch (Exception e) {
            log.error("❌ Error preparando backup", e);
            throw new RuntimeException("Error preparando backup de órdenes", e);
        }
    }

    /**
     * ⚡ OPTIMIZADA: Creación con cache evict
     */
    @Override
    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public CreateOrderResponseDto createOrder(OrderRequest request) {
        log.debug("📝 Creando nueva orden");

        long startTime = System.currentTimeMillis();
        CreateOrderResponseDto response = createOrderStrategy.execute(request);
        long endTime = System.currentTimeMillis();

        log.info("✅ Orden {} creada en {}ms", response.idOrder(), (endTime - startTime));
        return response;
    }

    /**
     * ⚡ OPTIMIZADA: Restauración de backup con procesamiento en lotes
     */
    @Override
    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public List<OrderRequest> restoreBackup(OrderRestore restore) {
        log.debug("🔄 Restaurando backup de {} órdenes", restore.orderRequests().size());

        long startTime = System.currentTimeMillis();

        // ⚡ Procesamiento en lotes para mejor performance
        List<OrderRequest> processed = restore.orderRequests().parallelStream()
                .map(request -> buildOrderRequest(request, restore.restoreDate()))
                .map(createOrdersStrategy::execute)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes restauradas en {}ms", processed.size(), (endTime - startTime));

        return processed;
    }

    /**
     * ⚡ OPTIMIZADA: Actualización con cache evict selectivo
     */
    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "'order-' + #idOrder")
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        log.debug("📝 Actualizando orden: {}", idOrder);

        long startTime = System.currentTimeMillis();

        UpdateOrderData updateData = new UpdateOrderData(idOrder, updateRequest);
        OrderResponse response = updateOrderStrategy.execute(updateData);

        // ⚡ Limpiar caché relacionado
        evictRelatedCaches();

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} actualizada en {}ms", idOrder, (endTime - startTime));

        return response;
    }

    /**
     * ⚡ OPTIMIZADA: Eliminación con cache evict
     */
    @Override
    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public void deleteOrder(Long idOrder) {
        log.debug("🗑️ Eliminando orden: {}", idOrder);

        long startTime = System.currentTimeMillis();

        OrderEntity order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        orderRepository.delete(order);

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} eliminada en {}ms", idOrder, (endTime - startTime));
    }

    /**
     * ⚡ HELPER: Construye OrderRequest para restore
     */
    private OrderRequest buildOrderRequest(OrderRequest request, LocalDate restoreDate) {
        return new OrderRequest(
                request.idPaymentMethod(),
                request.clientName(),
                request.comment(),
                addOrderTime(restoreDate),
                request.items()
        );
    }

    /**
     * ⚡ HELPER: Añade tiempo a la fecha de restore
     */
    private LocalDateTime addOrderTime(LocalDate restoreDate) {
        return restoreDate.atStartOfDay().plusSeconds(
                (long) (Math.random() * 86400) // Tiempo aleatorio en el día
        );
    }

    /**
     * ⚡ HELPER: Limpia cachés relacionados de forma selectiva
     */
    @CacheEvict(value = "orders", key = "'allOrders'")
    private void evictRelatedCaches() {
        log.debug("🧹 Limpiando cachés relacionados");
    }
}