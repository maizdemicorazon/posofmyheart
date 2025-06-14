package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.dtos.projection.OrderProjection;
import com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections;
import com.mdmc.posofmyheart.application.services.SalesCriteriaService;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que usa Criteria API para consultas complejas
 * Mantiene independencia de base de datos
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class SalesCriteriaServiceImpl implements SalesCriteriaService {

    private final EntityManager entityManager;

    /**
     * Obtiene ventas por categoría usando Criteria API
     */
    @Override
    public List<SalesReportProjections.CategorySalesProjection> findCategorySalesWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate) {

        log.debug("Finding category sales using Criteria API from {} to {}", startDate, endDate);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<OrderEntity> orderRoot = query.from(OrderEntity.class);
        Join<OrderEntity, OrderDetailEntity> orderDetailJoin = orderRoot.join("orderDetails");
        Join<OrderDetailEntity, ProductEntity> productJoin = orderDetailJoin.join("product");
        Join<ProductEntity, ProductCategoryEntity> categoryJoin = productJoin.join("category");

        // Selecciones con tipos específicos
        Expression<String> categoryName = categoryJoin.get("name");
        Expression<BigDecimal> totalSales = cb.sum(orderDetailJoin.get("sellPrice"));
        Expression<Long> totalOrders = cb.count(orderDetailJoin.get("idOrderDetail"));

        query.multiselect(
                categoryName.alias("categoryName"),
                totalSales.alias("totalSales"),
                totalOrders.alias("totalOrders")
        );

        // Condiciones
        Predicate datePredicate = cb.and(
                cb.greaterThanOrEqualTo(orderRoot.get("orderDate"), startDate),
                cb.lessThan(orderRoot.get("orderDate"), endDate)
        );
        query.where(datePredicate);

        // Agrupación
        query.groupBy(categoryJoin.get("idCategory"), categoryJoin.get("name"));

        // Ordenación
        query.orderBy(cb.desc(totalSales));

        List<Tuple> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .map(tuple -> new SalesReportProjections.CategorySalesProjection(
                        tuple.get("categoryName", String.class),
                        tuple.get("totalSales", BigDecimal.class),
                        tuple.get("totalOrders", Long.class)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene resumen del período usando Criteria API
     */
    @Override
    public SalesReportProjections.PeriodSummaryProjection findPeriodSummaryWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate) {

        log.debug("Finding period summary using Criteria API from {} to {}", startDate, endDate);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<OrderEntity> orderRoot = query.from(OrderEntity.class);

        // Selecciones con tipos específicos
        Expression<BigDecimal> totalSales = cb.sum(orderRoot.get("totalAmount"));
        Expression<Long> totalOrders = cb.count(orderRoot.get("idOrder"));

        query.multiselect(
                totalSales.alias("totalSales"),
                totalOrders.alias("totalOrders")
        );

        // Condiciones
        Predicate datePredicate = cb.and(
                cb.greaterThanOrEqualTo(orderRoot.get("orderDate"), startDate),
                cb.lessThan(orderRoot.get("orderDate"), endDate)
        );
        query.where(datePredicate);

        Tuple result = entityManager.createQuery(query).getSingleResult();

        return new SalesReportProjections.PeriodSummaryProjection(
                result.get("totalSales", BigDecimal.class),
                result.get("totalOrders", Long.class)
        );
    }

    /**
     * Obtiene órdenes con detalles usando Criteria API para análisis complejo
     */
    @Override
    public List<OrderProjection> findDetailedOrdersWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate) {

        log.debug("Finding detailed orders using Criteria API from {} to {}", startDate, endDate);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<OrderEntity> orderRoot = query.from(OrderEntity.class);
        Join<OrderEntity, OrderDetailEntity> orderDetailJoin = orderRoot.join("orderDetails", JoinType.LEFT);
        Join<OrderDetailEntity, ProductEntity> productJoin = orderDetailJoin.join("product", JoinType.LEFT);
        Join<ProductEntity, ProductCategoryEntity> categoryJoin = productJoin.join("category", JoinType.LEFT);
        Join<OrderEntity, PaymentMethodEntity> paymentJoin = orderRoot.join("paymentMethod", JoinType.LEFT);

        // Selecciones con tipos específicos
        Expression<LocalDateTime> orderDate = orderRoot.get("orderDate");
        Expression<BigDecimal> totalAmount = orderRoot.get("totalAmount");
        Expression<Long> orderId = orderRoot.get("idOrder");
        Expression<String> categoryName = categoryJoin.get("name");
        Expression<BigDecimal> sellPrice = orderDetailJoin.get("sellPrice");
        Expression<String> clientName = orderRoot.get("clientName");
        Expression<String> paymentMethodName = paymentJoin.get("name");

        query.multiselect(
                orderDate.alias("orderDate"),
                totalAmount.alias("totalAmount"),
                orderId.alias("orderId"),
                categoryName.alias("categoryName"),
                sellPrice.alias("sellPrice"),
                clientName.alias("clientName"),
                paymentMethodName.alias("paymentMethodName")
        );

        // Condiciones
        Predicate datePredicate = cb.and(
                cb.greaterThanOrEqualTo(orderRoot.get("orderDate"), startDate),
                cb.lessThan(orderRoot.get("orderDate"), endDate)
        );
        query.where(datePredicate);

        // Ordenación
        query.orderBy(cb.asc(orderRoot.get("orderDate")));

        List<Tuple> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .map(tuple -> new OrderProjection(
                        tuple.get("orderDate", LocalDateTime.class),
                        tuple.get("totalAmount", BigDecimal.class),
                        tuple.get("orderId", Long.class),
                        tuple.get("categoryName", String.class),
                        tuple.get("sellPrice", BigDecimal.class),
                        tuple.get("clientName", String.class),
                        tuple.get("paymentMethodName", String.class)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene órdenes básicas usando Criteria API
     * Útil para análisis temporal donde no se necesitan joins complejos
     */
    @Override
    public List<OrderProjection> findBasicOrdersWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate) {

        log.debug("Finding basic orders using Criteria API from {} to {}", startDate, endDate);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<OrderEntity> orderRoot = query.from(OrderEntity.class);

        // Selecciones básicas con tipos específicos
        Expression<LocalDateTime> orderDate = orderRoot.get("orderDate");
        Expression<BigDecimal> totalAmount = orderRoot.get("totalAmount");
        Expression<Long> orderId = orderRoot.get("idOrder");

        query.multiselect(
                orderDate.alias("orderDate"),
                totalAmount.alias("totalAmount"),
                orderId.alias("orderId")
        );

        // Condiciones
        Predicate datePredicate = cb.and(
                cb.greaterThanOrEqualTo(orderRoot.get("orderDate"), startDate),
                cb.lessThan(orderRoot.get("orderDate"), endDate)
        );
        query.where(datePredicate);

        // Ordenación
        query.orderBy(cb.asc(orderRoot.get("orderDate")));

        List<Tuple> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .map(tuple -> new OrderProjection(
                        tuple.get("orderDate", LocalDateTime.class),
                        tuple.get("totalAmount", BigDecimal.class),
                        tuple.get("orderId", Long.class)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Versión alternativa usando TypedQuery en lugar de Tuple
     * Para casos donde prefieras evitar el manejo manual de Tuple
     */
    @Override
    public List<SalesReportProjections.CategorySalesProjection> findCategorySalesTypedQuery(
            LocalDateTime startDate, LocalDateTime endDate) {

        log.debug("Finding category sales using TypedQuery from {} to {}", startDate, endDate);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<OrderEntity> orderRoot = query.from(OrderEntity.class);
        Join<OrderEntity, OrderDetailEntity> orderDetailJoin = orderRoot.join("orderDetails");
        Join<OrderDetailEntity, ProductEntity> productJoin = orderDetailJoin.join("product");
        Join<ProductEntity, ProductCategoryEntity> categoryJoin = productJoin.join("category");

        // Selección como array de Object
        query.multiselect(
                categoryJoin.get("name"),
                cb.sum(orderDetailJoin.get("sellPrice")),
                cb.count(orderDetailJoin.get("idOrderDetail"))
        );

        // Condiciones
        Predicate datePredicate = cb.and(
                cb.greaterThanOrEqualTo(orderRoot.get("orderDate"), startDate),
                cb.lessThan(orderRoot.get("orderDate"), endDate)
        );
        query.where(datePredicate);

        // Agrupación
        query.groupBy(categoryJoin.get("idCategory"), categoryJoin.get("name"));

        // Ordenación
        query.orderBy(cb.desc(cb.sum(orderDetailJoin.get("sellPrice"))));

        List<Object[]> results = entityManager.createQuery(query).getResultList();

        return results.stream()
                .map(row -> new SalesReportProjections.CategorySalesProjection(
                        (String) row[0],
                        (BigDecimal) row[1],
                        (Long) row[2]
                ))
                .collect(Collectors.toList());
    }
}