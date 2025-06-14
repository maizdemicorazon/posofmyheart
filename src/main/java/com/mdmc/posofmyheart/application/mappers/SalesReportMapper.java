package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.CategoryAnalysisData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Mapper para transformaciones de datos de reportes de ventas
 * Usa MapStruct para mapeos automáticos y métodos personalizados
 */
@Mapper(componentModel = "spring")
public interface SalesReportMapper {

    /**
     * Mapea proyección de categorías a DTO de análisis
     */
    @Mapping(target = "name", source = "categoryName")
    @Mapping(target = "sales", source = "totalSales")
    @Mapping(target = "orders", source = "totalOrders", qualifiedByName = "longToInteger")
    @Mapping(target = "percentage", ignore = true) // Se calcula en el servicio
    CategoryAnalysisData toCategoryAnalysisData(SalesReportProjections.CategorySalesProjection projection);

    /**
     * Mapea proyección de resumen a valores básicos
     */
    @Mapping(target = "totalSales", source = "totalSales")
    @Mapping(target = "totalOrders", source = "totalOrders", qualifiedByName = "longToInteger")
    PeriodSummaryData toPeriodSummaryData(SalesReportProjections.PeriodSummaryProjection projection);

    @Named("longToInteger")
    default Integer longToInteger(Long value) {
        return value != null ? value.intValue() : 0;
    }

    @Named("calculateAverageTicket")
    default BigDecimal calculateAverageTicket(BigDecimal totalSales, Long totalOrders) {
        if (totalOrders == null || totalOrders == 0) {
            return BigDecimal.ZERO;
        }
        return totalSales.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);
    }

    @Named("calculatePercentage")
    default Double calculatePercentage(BigDecimal amount, BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return amount.divide(total, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    @Named("safeBigDecimal")
    default BigDecimal safeBigDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    @Named("safeLong")
    default Long safeLong(Long value) {
        return value != null ? value : 0L;
    }

    // DTO auxiliar para resumen de período
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PeriodSummaryData {
        private BigDecimal totalSales;
        private Integer totalOrders;

        public PeriodSummaryData(BigDecimal totalSales, Integer totalOrders) {
            this.totalSales = totalSales;
            this.totalOrders = totalOrders;
        }
    }
}