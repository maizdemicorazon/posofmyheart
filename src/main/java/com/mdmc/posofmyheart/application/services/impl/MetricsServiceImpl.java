package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;
import com.mdmc.posofmyheart.application.dtos.builders.DailyEarningsBuilder;
import com.mdmc.posofmyheart.application.mappers.DailyEarningsResponseMapper;
import com.mdmc.posofmyheart.application.services.MetricsService;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    public static final int DEFAULT_BACK_DAYS = 0;
    private final OrderRepository orderRepository;
    private final OrderCalculationService orderCalculationService;

    @Override
    public DailyEarningsResponse getDailyEarnings(Integer backDays) {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(backDays), LocalTime.MIN);
        List<OrderEntity> orders = orderRepository.findByDateRange(startDate, LocalDateTime.now());

        List<DailyEarnings> dailyEarnings = groupOrdersByDate(orders)
                .entrySet()
                .stream()
                .map(entry -> DailyEarningsBuilder.fromEntry(entry, orderCalculationService))
                .sorted(Comparator.comparing(DailyEarnings::date).reversed())
                .toList();

        return DailyEarningsResponseMapper.INSTANCE.toResponse(dailyEarnings);
    }

    @Override
    public DailyEarningsResponse getTodayDailyEarnings() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(DEFAULT_BACK_DAYS), LocalTime.MIN);
        List<OrderEntity> orders = orderRepository.findByDateRange(startDate, LocalDateTime.now());

        List<DailyEarnings> dailyEarnings = groupOrdersByDate(orders)
                .entrySet()
                .stream()
                .map(entry -> DailyEarningsBuilder.fromEntry(entry, orderCalculationService))
                .sorted(Comparator.comparing(DailyEarnings::date).reversed())
                .toList();

        return DailyEarningsResponseMapper.INSTANCE.toResponse(dailyEarnings);
    }

    @Override
    public DailyEarningsResponse getTodayDailyEarningsWithPercentage(BigDecimal profit) {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(DEFAULT_BACK_DAYS), LocalTime.MIN);
        List<OrderEntity> orders = orderRepository.findByDateRange(startDate, LocalDateTime.now());

        List<DailyEarnings> dailyEarnings = groupOrdersByDate(orders)
                .entrySet()
                .stream()
                .map(entry -> DailyEarningsBuilder
                        .fromEntry(
                                entry, orderCalculationService, profit)
                )
                .sorted(Comparator.comparing(DailyEarnings::date).reversed())
                .toList();

        return DailyEarningsResponseMapper.INSTANCE.toResponse(dailyEarnings);
    }

    private Map<LocalDate, List<OrderEntity>> groupOrdersByDate(List<OrderEntity> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toLocalDate()
                ));
    }

}