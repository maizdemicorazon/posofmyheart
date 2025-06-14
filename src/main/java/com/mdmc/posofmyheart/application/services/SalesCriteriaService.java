package com.mdmc.posofmyheart.application.services;


import com.mdmc.posofmyheart.application.dtos.projection.OrderProjection;
import com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que usa Criteria API para consultas complejas
 * Mantiene independencia de base de datos
 */

public interface SalesCriteriaService {
    List<SalesReportProjections.CategorySalesProjection> findCategorySalesWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate);

    SalesReportProjections.PeriodSummaryProjection findPeriodSummaryWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate);

    List<OrderProjection> findDetailedOrdersWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate);

    List<OrderProjection> findBasicOrdersWithCriteria(
            LocalDateTime startDate, LocalDateTime endDate);

    List<SalesReportProjections.CategorySalesProjection> findCategorySalesTypedQuery(
            LocalDateTime startDate, LocalDateTime endDate);
}