package com.mdmc.posofmyheart.domain.dtos;

import java.math.BigDecimal;

public record ResultCommissionDto(
        Integer countCardPays,
        BigDecimal terminalDiscount,
        BigDecimal sellTerminal
) {
}
