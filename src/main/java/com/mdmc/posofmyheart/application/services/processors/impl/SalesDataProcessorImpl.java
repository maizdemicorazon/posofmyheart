package com.mdmc.posofmyheart.application.services.processors.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.application.dtos.projections.SalesOrderProjection;
import com.mdmc.posofmyheart.application.dtos.projections.SalesReportProjections;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.CategoryAnalysisData;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.DailySalesData;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.PeakHourData;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.WeekdayAnalysisData;
import com.mdmc.posofmyheart.application.services.processors.SalesDataProcessor;

/**
 * Procesador de datos de ventas usando programación nativa
 * Maneja análisis complejos que no se pueden hacer eficientemente en JPQL
 */
@Component
@Log4j2
public class SalesDataProcessorImpl implements SalesDataProcessor {

    private static final Locale SPANISH_LOCALE = new Locale("es", "ES");
    private static final DateTimeFormatter DATE_FORMATTER_PHRASE =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", SPANISH_LOCALE);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy", SPANISH_LOCALE);

    /**
     * Procesa órdenes para crear datos diarios agrupados
     */
    @Override
    public List<DailySalesData> processDailySalesData(List<SalesOrderProjection> orders) {
        log.debug("Processing daily sales data for {} orders", orders.size());

        Map<LocalDate, DailySalesModel> dailyDataMap = orders.stream()
                .collect(Collectors.groupingBy(
                        SalesOrderProjection::getOrderLocalDate,
                        LinkedHashMap::new,
                        Collectors.reducing(
                                new DailySalesModel(),
                                order -> new DailySalesModel(order.getSafeTotalAmount(), 1L),
                                DailySalesModel::combine
                        )
                ));

        return dailyDataMap.entrySet().stream()
                .map(entry -> createDailySalesData(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Procesa órdenes para análisis por días de la semana
     */
    @Override
    public List<WeekdayAnalysisData> processWeekdayAnalysis(List<SalesOrderProjection> orders) {
        log.debug("Processing weekday analysis for {} orders", orders.size());

        Map<DayOfWeek, WeekdayModel> weekdayDataMap = orders.stream()
                .collect(Collectors.groupingBy(
                        SalesOrderProjection::getOrderDayOfWeek,
                        () -> new EnumMap<>(DayOfWeek.class),
                        Collectors.reducing(
                                new WeekdayModel(),
                                order -> new WeekdayModel(
                                        order.getSafeTotalAmount(),
                                        1L,
                                        Set.of(order.getOrderLocalDate())
                                ),
                                WeekdayModel::combine
                        )
                ));

        return Arrays.stream(DayOfWeek.values())
                .map(dayOfWeek -> createWeekdayAnalysisData(dayOfWeek, weekdayDataMap.get(dayOfWeek)))
                .filter(data -> data.getTotalOrders() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Procesa órdenes para encontrar horarios pico
     */
    @Override
    public List<PeakHourData> processPeakHours(List<SalesOrderProjection> orders, BigDecimal totalSales) {
        log.debug("Processing peak hours for {} orders", orders.size());

        Map<Integer, HourModel> hourDataMap = orders.stream()
                .filter(order -> order.getOrderHour() != null)
                .collect(Collectors.groupingBy(
                        SalesOrderProjection::getOrderHour,
                        Collectors.reducing(
                                new HourModel(),
                                order -> new HourModel(order.getSafeTotalAmount(), 1L),
                                HourModel::combine
                        )
                ));

        return hourDataMap.entrySet().stream()
                .map(entry -> createPeakHourData(entry.getKey(), entry.getValue(), totalSales))
                .sorted(Comparator.comparing(PeakHourData::getSales).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Calcula porcentajes para análisis de categorías
     */
    @Override
    public List<CategoryAnalysisData> processCategories(
            List<SalesReportProjections.CategorySalesProjection> categories, BigDecimal totalSales) {

        log.debug("Processing category analysis for {} categories", categories.size());

        return categories.stream()
                .map(category -> CategoryAnalysisData.builder()
                        .name(category.categoryName())
                        .sales(category.totalSales())
                        .orders(category.totalOrders().intValue())
                        .percentage(calculatePercentage(category.totalSales(), totalSales))
                        .build())
                .collect(Collectors.toList());
    }

    // Métodos auxiliares privados

    private PeakHourData createPeakHourData(Integer hour, HourModel model, BigDecimal totalSales) {
        return PeakHourData.builder()
                .hour(String.format("%02d:00-%02d:00", hour, hour + 1))
                .sales(model.getTotalSales())
                .percentage(calculatePercentage(model.getTotalSales(), totalSales))
                .build();
    }

    private Double calculatePercentage(BigDecimal amount, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return amount.divide(total, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    private WeekdayAnalysisData createWeekdayAnalysisData(DayOfWeek dayOfWeek, WeekdayModel model) {
        if (model == null) {
            model = new WeekdayModel();
        }

        int occurrences = model.getUniqueDates().size();

        return WeekdayAnalysisData.builder()
                .day(dayOfWeek.getDisplayName(TextStyle.FULL, SPANISH_LOCALE))
                .totalSales(model.getTotalSales())
                .totalOrders(model.getTotalOrders().intValue())
                .averageSales(occurrences > 0 ?
                        model.getTotalSales().divide(BigDecimal.valueOf(occurrences), 2, RoundingMode.HALF_UP) :
                        BigDecimal.ZERO)
                .averageOrders(occurrences > 0 ? model.getTotalOrders().intValue() / occurrences : 0)
                .build();
    }

    private DailySalesData createDailySalesData(LocalDate date, DailySalesModel model) {
        return DailySalesData.builder()
                .date(date.format(DATE_FORMATTER_PHRASE))
                .fullDate(date.format(DATE_FORMATTER))
                .sales(model.getTotalSales())
                .orders(model.getTotalOrders().intValue())
                .averageTicket(calculateAverageTicket(model.getTotalSales(), model.getTotalOrders()))
                .dayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.FULL, SPANISH_LOCALE))
                .build();
    }

    private BigDecimal calculateAverageTicket(BigDecimal totalSales, Long totalOrders) {
        if (totalOrders == 0) {
            return BigDecimal.ZERO;
        }
        return totalSales.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);
    }

    // Modelos internos para agrupación
    @Data
    @NoArgsConstructor
    public static class DailySalesModel {
        private BigDecimal totalSales = BigDecimal.ZERO;
        private Long totalOrders = 0L;

        public DailySalesModel(BigDecimal sales, Long orders) {
            this.totalSales = sales != null ? sales : BigDecimal.ZERO;
            this.totalOrders = orders != null ? orders : 0L;
        }

        public DailySalesModel combine(DailySalesModel other) {
            return new DailySalesModel(
                    this.totalSales.add(other.totalSales),
                    this.totalOrders + other.totalOrders
            );
        }
    }

    @Data
    @NoArgsConstructor
    public static class WeekdayModel {
        private BigDecimal totalSales = BigDecimal.ZERO;
        private Long totalOrders = 0L;
        private Set<LocalDate> uniqueDates = new HashSet<>();

        public WeekdayModel(BigDecimal sales, Long orders, Set<LocalDate> dates) {
            this.totalSales = sales != null ? sales : BigDecimal.ZERO;
            this.totalOrders = orders != null ? orders : 0L;
            this.uniqueDates = dates != null ? new HashSet<>(dates) : new HashSet<>();
        }

        public WeekdayModel combine(WeekdayModel other) {
            Set<LocalDate> combinedDates = new HashSet<>(this.uniqueDates);
            combinedDates.addAll(other.uniqueDates);

            return new WeekdayModel(
                    this.totalSales.add(other.totalSales),
                    this.totalOrders + other.totalOrders,
                    combinedDates
            );
        }
    }

    @Data
    @NoArgsConstructor
    public static class HourModel {
        private BigDecimal totalSales = BigDecimal.ZERO;
        private Long totalOrders = 0L;

        public HourModel(BigDecimal sales, Long orders) {
            this.totalSales = sales != null ? sales : BigDecimal.ZERO;
            this.totalOrders = orders != null ? orders : 0L;
        }

        public HourModel combine(HourModel other) {
            return new HourModel(
                    this.totalSales.add(other.totalSales),
                    this.totalOrders + other.totalOrders
            );
        }
    }
}