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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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
    private final CacheManager cacheManager;

    /**
     * ⚡ SÚPER OPTIMIZADA: Una sola query para todas las órdenes con EntityGraph
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'allOrders'")
    public List<OrderResponse> findAllOrders() {
        log.debug("🔍 Obteniendo todas las órdenes con EntityGraph optimizado");

        long startTime = System.currentTimeMillis();

        // ⚡ UNA SOLA QUERY con EntityGraph completo - ELIMINA N+1 por completo
        List<OrderEntity> orders = orderRepository.findAllWithCompleteDetails();

        // ⚡ Mapeo optimizado usando MapStruct
        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes obtenidas con EntityGraph en {}ms", responses.size(), (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ OPTIMIZADA: Búsqueda por fecha con EntityGraph y caché específico
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersByDate-' + #date.toString()")
    public List<OrderResponse> listOrdersByDate(LocalDate date) {
        log.debug("🔍 Obteniendo órdenes para fecha: {} con EntityGraph", date);

        long startTime = System.currentTimeMillis();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // ⚡ Query optimizada con EntityGraph
        var orders = orderRepository.findByOrderDate(startOfDay, endOfDay);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes obtenidas para {} con EntityGraph en {}ms", responses.size(), date, (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ OPTIMIZADA: Búsqueda por ID con EntityGraph y caché específico
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'order-' + #idOrder")
    public OrderResponse findOrderById(Long idOrder) {
        log.debug("🔍 Obteniendo orden por ID: {} con EntityGraph", idOrder);

        long startTime = System.currentTimeMillis();

        // ⚡ Query optimizada con EntityGraph completo
        OrderEntity order = orderRepository.findByIdWithCompleteDetails(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        OrderResponse response = OrderResponseMapper.INSTANCE.toResponse(order);

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} obtenida con EntityGraph en {}ms", idOrder, (endTime - startTime));

        return response;
    }

    /**
     * ⚡ NUEVO: Búsqueda ligera por ID (solo datos básicos) - Package private
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'orderBasic-' + #idOrder")
    OrderResponse findOrderByIdBasic(Long idOrder) {
        log.debug("🔍 Obteniendo orden básica por ID: {}", idOrder);

        long startTime = System.currentTimeMillis();

        // ⚡ EntityGraph básico para datos ligeros
        OrderEntity order = orderRepository.findByIdBasic(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        OrderResponse response = OrderResponseMapper.INSTANCE.toResponse(order);

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden básica {} obtenida en {}ms", idOrder, (endTime - startTime));

        return response;
    }

    /**
     * ⚡ NUEVO: Órdenes con detalles intermedios (sin extras/salsas/sabores) - Package private
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersWithDetails'")
    List<OrderResponse> findAllOrdersWithDetails() {
        log.debug("🔍 Obteniendo órdenes con detalles intermedios");

        long startTime = System.currentTimeMillis();

        // ⚡ EntityGraph intermedio
        List<OrderEntity> orders = orderRepository.findAllWithOrderDetails();

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes con detalles obtenidas en {}ms", responses.size(), (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ NUEVO: Búsqueda por cliente - Package private
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersByClient-' + #clientName")
    List<OrderResponse> findOrdersByClient(String clientName) {
        log.debug("🔍 Obteniendo órdenes para cliente: {}", clientName);

        long startTime = System.currentTimeMillis();

        List<OrderEntity> orders = orderRepository.findByClientNameContainingIgnoreCase(clientName);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes encontradas para cliente '{}' en {}ms", responses.size(), clientName, (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ NUEVO: Búsqueda por rango de totales - Package private
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersByRange-' + #minAmount + '-' + #maxAmount")
    List<OrderResponse> findOrdersByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        log.debug("🔍 Obteniendo órdenes entre ${} y ${}", minAmount, maxAmount);

        long startTime = System.currentTimeMillis();

        List<OrderEntity> orders = orderRepository.findByTotalAmountBetween(minAmount, maxAmount);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes encontradas en rango ${}-${} en {}ms", responses.size(), minAmount, maxAmount, (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ NUEVO: Búsqueda por método de pago - Package private
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'ordersByPayment-' + #paymentMethodId")
    List<OrderResponse> findOrdersByPaymentMethod(Long paymentMethodId) {
        log.debug("🔍 Obteniendo órdenes para método de pago: {}", paymentMethodId);

        long startTime = System.currentTimeMillis();

        List<OrderEntity> orders = orderRepository.findByPaymentMethodId(paymentMethodId);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes encontradas para método de pago {} en {}ms", responses.size(), paymentMethodId, (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ NUEVO: Órdenes recientes (últimas N) - Package private
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'recentOrders-' + #limit")
    List<OrderResponse> findRecentOrders(int limit) {
        log.debug("🔍 Obteniendo últimas {} órdenes", limit);

        long startTime = System.currentTimeMillis();

        List<OrderEntity> orders = orderRepository.findRecentOrders(limit);

        List<OrderResponse> responses = orders.stream()
                .map(OrderResponseMapper.INSTANCE::toResponse)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes recientes obtenidas en {}ms", responses.size(), (endTime - startTime));

        return responses;
    }

    /**
     * ⚡ DASHBOARD: Estadísticas rápidas sin cargar entidades completas - Package private
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'stats-' + #date.toString()")
    OrderStatsResponse getOrderStatsByDate(LocalDate date) {
        log.debug("🔍 Obteniendo estadísticas para fecha: {}", date);

        long startTime = System.currentTimeMillis();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // ⚡ Queries optimizadas solo para conteo y suma (sin cargar entidades)
        Long orderCount = orderRepository.countOrdersByDate(startOfDay, endOfDay);
        BigDecimal totalAmount = orderRepository.sumTotalAmountByDate(startOfDay, endOfDay);

        OrderStatsResponse stats = new OrderStatsResponse(date, orderCount, totalAmount);

        long endTime = System.currentTimeMillis();
        log.info("✅ Estadísticas para {} obtenidas en {}ms", date, (endTime - startTime));

        return stats;
    }

    /**
     * ⚡ BACKUP: Procesamiento paralelo optimizado con EntityGraph
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "orders", key = "'backup'")
    public OrderRestore findOrdersToBackup() {
        log.debug("🔍 Preparando backup de órdenes con EntityGraph");

        long startTime = System.currentTimeMillis();

        // ⚡ Procesamiento asíncrono para backup
        CompletableFuture<List<OrderEntity>> ordersFuture = CompletableFuture
                .supplyAsync(orderRepository::findAllWithCompleteDetails);

        try {
            List<OrderEntity> orders = ordersFuture.get();
            List<OrderResponse> orderResponses = orders.stream()
                    .map(OrderResponseMapper.INSTANCE::toResponse)
                    .toList();

            // ⚡ Usar el mapper existente correctamente
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
     * ⚡ CREAR ORDEN: Con invalidación de caché inteligente usando estrategia
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'")
    })
    public CreateOrderResponseDto createOrder(OrderRequest request) {
        log.debug("🚀 Creando nueva orden usando estrategia");

        long startTime = System.currentTimeMillis();

        // ⚡ Usar estrategia para crear orden
        CreateOrderResponseDto response = createOrderStrategy.execute(request);

        // 🧹 Invalidar caché de patrones relacionados
        evictCachePatterns("recentOrders-", "stats-");

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} creada en {}ms", response.idOrder(), (endTime - startTime));

        return response;
    }

    /**
     * ⚡ ACTUALIZAR ORDEN: Con invalidación de caché específica usando estrategia
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'"),
            @CacheEvict(value = "orders", key = "'order-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'orderBasic-' + #idOrder")
    })
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        log.debug("🔄 Actualizando orden: {} usando estrategia", idOrder);

        long startTime = System.currentTimeMillis();

        // ⚡ Crear UpdateOrderData y usar estrategia
        UpdateOrderData updateData = new UpdateOrderData(idOrder, updateRequest);
        OrderResponse response = updateOrderStrategy.execute(updateData);

        // 🧹 Invalidar caché de patrones relacionados
        evictCachePatterns("recentOrders-", "stats-");

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} actualizada en {}ms", idOrder, (endTime - startTime));

        return response;
    }

    /**
     * ⚡ ELIMINAR ORDEN: Con invalidación específica de caché
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'"),
            @CacheEvict(value = "orders", key = "'order-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'orderBasic-' + #idOrder")
    })
    public void deleteOrder(Long idOrder) {
        log.debug("🗑️ Eliminando orden: {}", idOrder);

        long startTime = System.currentTimeMillis();

        // ⚡ Verificar que existe antes de eliminar usando método optimizado
        if (!orderRepository.existsByIdOrder(idOrder)) {
            throw new OrderNotFoundException(idOrder);
        }

        orderRepository.deleteById(idOrder);

        // 🧹 Invalidar caché dinámico que podría incluir esta orden
        evictDynamicCacheEntries();

        long endTime = System.currentTimeMillis();
        log.info("✅ Orden {} eliminada en {}ms", idOrder, (endTime - startTime));
    }

    /**
     * ⚡ RESTAURAR BACKUP: Con invalidación específica usando estrategia
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'")
    })
    public List<OrderRequest> restoreBackup(OrderRestore restore) {
        log.debug("🔄 Restaurando backup de {} órdenes usando estrategia", restore.orderRequests().size());

        long startTime = System.currentTimeMillis();

        // ⚡ Procesamiento en lotes usando estrategia para mejor performance
        List<OrderRequest> processed = restore.orderRequests().parallelStream()
                .map(request -> buildOrderRequest(request, restore.restoreDate()))
                .map(createOrdersStrategy::execute)
                .toList();

        // 🧹 Invalidar todo el caché dinámico después de restaurar
        evictDynamicCacheEntries();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} órdenes restauradas en {}ms", processed.size(), (endTime - startTime));

        return processed;
    }

    /**
     * ⚡ VERIFICAR EXISTENCIA: Método ligero - Package private
     */
    @Transactional(readOnly = true)
    boolean orderExists(Long idOrder) {
        return orderRepository.existsByIdOrder(idOrder);
    }

    /**
     * ⚡ Helper method: Construye OrderRequest para restore con fecha actualizada
     */
    private OrderRequest buildOrderRequest(OrderRequest request, LocalDate restoreDate) {
        return new OrderRequest(
                request.idPaymentMethod(),
                request.clientName(),
                request.comment(),
                OrderRestore.addOrderTime(restoreDate),
                request.items()
        );
    }

    /**
     * 🧹 Helper method: Invalidar caché por patrones específicos
     * Spring Cache no soporta patrones directamente, así que lo hacemos programáticamente
     */
    private void evictCachePatterns(String... patterns) {
        try {
            var cache = cacheManager.getCache("orders");
            if (cache != null) {
                var nativeCache = cache.getNativeCache();

                // Si es Caffeine Cache (configurado en application.properties)
                if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache) {
                    @SuppressWarnings("unchecked")
                    var caffeineCache = (com.github.benmanes.caffeine.cache.Cache<Object, Object>) nativeCache;

                    // Buscar todas las claves que coincidan con los patrones
                    caffeineCache.asMap().keySet().removeIf(key -> {
                        String keyStr = String.valueOf(key);
                        return Arrays.stream(patterns)
                                .anyMatch(keyStr::startsWith);
                    });

                    log.debug("🧹 Caffeine cache patterns invalidated: {}", Arrays.toString(patterns));
                } else {
                    // Fallback: limpiar todo el cache si no es Caffeine
                    cache.clear();
                    log.debug("🧹 Entire cache cleared (fallback for non-Caffeine cache)");
                }
            }
        } catch (Exception e) {
            log.warn("⚠️ Error invalidating cache patterns, clearing entire cache: {}", e.getMessage());
            var cache = cacheManager.getCache("orders");
            if (cache != null) {
                cache.clear();
            }
        }
    }

    /**
     * 🧹 Helper method: Invalidar todas las entradas de caché relacionadas con estadísticas y órdenes recientes
     */
    private void evictDynamicCacheEntries() {
        evictCachePatterns("recentOrders-", "stats-", "ordersByDate-", "ordersByClient-",
                "ordersByRange-", "ordersByPayment-");
    }

    // ===== MÉTODOS ADICIONALES PARA USO INTERNO =====
    // Los métodos anteriores no públicos son para uso interno del servicio
    // y futuras extensiones de funcionalidad sin cambiar el contrato público

    // Record público para estadísticas rápidas
    public record OrderStatsResponse(
            LocalDate date,
            Long orderCount,
            BigDecimal totalAmount
    ) {}
}