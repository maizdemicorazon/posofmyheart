package com.mdmc.posofmyheart.application.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;
import com.mdmc.posofmyheart.application.dtos.builders.DailyEarningsBuilder;
import com.mdmc.posofmyheart.application.mappers.DailyEarningsResponseMapper;
import com.mdmc.posofmyheart.application.services.MetricsService;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;

@Service
@AllArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    public static final int DEFAULT_BACK_DAYS = 0;
    private final OrderRepository orderRepository;
    private final OrderCalculationService orderCalculationService;

    @Override
    public DailyEarningsResponse getDailyEarnings(Integer backDays) {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(backDays), LocalTime.MIN);
        Set<OrderEntity> orders = orderRepository.findByCreatedAt(startDate, LocalDateTime.now());

        Set<DailyEarnings> dailyEarnings = groupOrdersByDate(orders)
                .entrySet()
                .stream()
                .map(entry -> DailyEarningsBuilder.fromEntry(entry, orderCalculationService))
                .collect(Collectors.toSet());

        return DailyEarningsResponseMapper.INSTANCE.toResponse(dailyEarnings);
    }

    @Override
    public DailyEarningsResponse getTodayDailyEarnings() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(DEFAULT_BACK_DAYS), LocalTime.MIN);
        Set<OrderEntity> orders = orderRepository.findByCreatedAt(startDate, LocalDateTime.now());

        Set<DailyEarnings> dailyEarnings = groupOrdersByDate(orders)
                .entrySet()
                .stream()
                .map(entry -> DailyEarningsBuilder.fromEntry(entry, orderCalculationService))
                .collect(Collectors.toSet());

        return DailyEarningsResponseMapper.INSTANCE.toResponse(dailyEarnings);
    }

    @Override
    public DailyEarningsResponse getTodayDailyEarningsWithPercentage(BigDecimal profit) {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(DEFAULT_BACK_DAYS), LocalTime.MIN);
        Set<OrderEntity> orders = orderRepository.findByCreatedAt(startDate, LocalDateTime.now());

        Set<DailyEarnings> dailyEarnings = groupOrdersByDate(orders)
                .entrySet()
                .stream()
                .map(entry -> DailyEarningsBuilder
                        .fromEntry(
                                entry, orderCalculationService, profit)
                )
                .collect(Collectors.toSet());

        return DailyEarningsResponseMapper.INSTANCE.toResponse(dailyEarnings);
    }

    private Map<LocalDate, Set<OrderEntity>> groupOrdersByDate(Set<OrderEntity> orders) {
        Map<LocalDate, Set<OrderEntity>> map = new HashMap<>();
        for (OrderEntity order : orders) {
            map.computeIfAbsent(order.getCreatedAt().toLocalDate(), k -> new HashSet<>()).add(order);
        }
        return map;
    }

}