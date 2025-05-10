package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.domain.models.Product;

import java.math.BigDecimal;

public record ProductWithMarginResponse(
        Product product,
        BigDecimal profitMargin,
        BigDecimal profitMarginPercentage
) {}