package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.OrderNotFoundException;
import com.mdmc.posofmyheart.application.dtos.*;
import com.mdmc.posofmyheart.application.mappers.OrderResponseMapper;
import com.mdmc.posofmyheart.application.mappers.OrderRestoreMapper;
import com.mdmc.posofmyheart.application.services.CacheService;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.domain.patterns.strategies.CreateOrderStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.CreateOrdersStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.UpdateOrderStrategy;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final CacheService cacheService;

    /**
     * Una sola query para todas las órdenes con EntityGraph
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'allOrders'")
    public List<OrderResponse> findAllOrders() {
        log.debug("🔍 Obteniendo todas las órdenes");

        long startTime = System.currentTimeMillis();

        // UNA SOLA QUERY con EntityGraph completo
        List<OrderEntity> orders = orderRepository.findAllWithDetails();

        //Mapeo optimizado usando MapStruct
        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes obtenidas con EntityGraph en {}ms", responses.size(), (endTime - startTime));

        return responses;
    }

    /**
     * Búsqueda por fecha con EntityGraph y caché específico
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersByDate-' + #date.toString()")
    public List<OrderResponse> findOrdersByDate(LocalDate date) {
        log.debug("🔍 Obteniendo órdenes de la fecha: {}", date);

        long startTime = System.currentTimeMillis();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // Query optimizada con EntityGraph
        var orders = orderRepository.findByOrderDate(startOfDay, endOfDay);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes obtenidas de la fecha {} en {}ms", responses.size(), date, (endTime - startTime));

        return responses;
    }

    /**
     * Búsqueda por fecha con EntityGraph y caché específico
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersByPeriod-' + #start.toString()")
    @Override
    public List<OrderResponse> findOrdersByPeriod(LocalDate start, LocalDate end) {
        log.debug("🔍 Obteniendo órdenes para el periodo entre: {} y {} días con EntityGraph", start, end);
        long startTime = System.currentTimeMillis();

        LocalDateTime startDate = start.atTime(LocalTime.MIN);
        LocalDateTime endDate = end.atTime(LocalTime.MAX);
        // Query optimizada con EntityGraph
        var orders = orderRepository.findByOrderDate(startDate, endDate);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes obtenidas de las fechas entre {} a {} en {}ms",
                responses.size(), startDate, endDate, (endTime - startTime));

        return responses;
    }

    /**
     * Búsqueda por ID con EntityGraph y caché específico
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'order-' + #idOrder")
    public OrderResponse findOrderById(Long idOrder) {
        log.debug("🔍 Obteniendo orden por ID: {}", idOrder);

        long startTime = System.currentTimeMillis();

        // Query optimizada con EntityGraph completo
        OrderEntity order = orderRepository.findByIdWithDetails(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        OrderResponse response = OrderResponseMapper.INSTANCE.toResponse(order);

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} obtenida en {}ms", idOrder, (endTime - startTime));

        return response;
    }

    /**
     * BACKUP: Procesamiento paralelo optimizado con EntityGraph
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'backup'")
    public OrderRestore findOrdersToBackup() {
        log.debug("🔍 Preparando backup de órdenes");

        long startTime = System.currentTimeMillis();

        // Procesamiento asíncrono para backup
        CompletableFuture<List<OrderEntity>> ordersFuture = CompletableFuture
                .supplyAsync(orderRepository::findAllWithDetails);

        try {
            List<OrderEntity> orders = ordersFuture.get();
            List<OrderResponse> orderResponses = orders.stream()
                    .map(OrderResponseMapper.INSTANCE::toResponse)
                    .toList();

            // Usar el mapper existente correctamente
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
     * CREAR ORDEN: Con invalidación de caché inteligente usando estrategia
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'"),
            @CacheEvict(value = "orders", key = "'ordersByPeriod'")
    })
    public CreateOrderResponseDto createOrder(OrderRequest request) {
        log.debug("🚀 Creando nueva orden usando estrategia");

        long startTime = System.currentTimeMillis();

        // Usar estrategia para crear orden
        CreateOrderResponseDto response = createOrderStrategy.execute(request);

        cacheService.clearCache();

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} creada en {}ms", response.idOrder(), (endTime - startTime));

        return response;
    }

    /**
     * ACTUALIZAR ORDEN: Con invalidación de caché específica usando estrategia
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'"),
            @CacheEvict(value = "orders", key = "'order-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'orderBasic-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'ordersByPeriod'")
    })
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        log.debug("🔄 Actualizando orden: {} usando estrategia", idOrder);

        long startTime = System.currentTimeMillis();

        // Crear UpdateOrderData y usar estrategia
        UpdateOrderData updateData = new UpdateOrderData(idOrder, updateRequest);
        OrderResponse response = updateOrderStrategy.execute(updateData);

        cacheService.clearCache();

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} actualizada en {}ms", idOrder, (endTime - startTime));

        return response;
    }

    /**
     * ELIMINAR ORDEN: Con invalidación específica de caché
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'"),
            @CacheEvict(value = "orders", key = "'order-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'orderBasic-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'ordersByPeriod'")
    })
    public void deleteOrder(Long idOrder) {
        log.debug("🗑️ Eliminando orden: {}", idOrder);

        long startTime = System.currentTimeMillis();

        // Verificar que existe antes de eliminar usando método optimizado
        if (!orderRepository.existsByIdOrder(idOrder)) {
            throw new OrderNotFoundException(idOrder);
        }

        orderRepository.deleteById(idOrder);

        cacheService.clearCache();

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} eliminada en {}ms", idOrder, (endTime - startTime));
    }

    /**
     * RESTAURAR BACKUP: Con invalidación específica usando estrategia
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'"),
            @CacheEvict(value = "orders", key = "'ordersByPeriod'")
    })
    public List<OrderRequest> restoreBackup(OrderRestore restore) {
        log.debug("🔄 Restaurando backup de {} órdenes usando estrategia", restore.orderRequests().size());

        long startTime = System.currentTimeMillis();

        // Procesamiento en lotes usando estrategia para mejor performance
        List<OrderRequest> processed = restore.orderRequests().parallelStream()
                .map(request -> buildOrderRequest(request, restore.restoreDate()))
                .map(createOrdersStrategy::execute)
                .toList();

        cacheService.clearCache();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes restauradas en {}ms", processed.size(), (endTime - startTime));

        return processed;
    }

    /**
     * Helper method: Construye OrderRequest para restore con fecha actualizada
     */
    private OrderRequest buildOrderRequest(OrderRequest request, LocalDate restoreDate) {
        return new OrderRequest(
                request.idPaymentMethod(),
                request.clientName(),
                OrderRestore.addOrderTime(restoreDate),
                request.items()
        );
    }

}