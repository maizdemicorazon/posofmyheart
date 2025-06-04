package com.mdmc.posofmyheart.domain.dtos;

import java.math.BigDecimal;

public record ResultCommission(
        Integer countCardPays,
        BigDecimal terminalDiscount,
        BigDecimal sellTerminal
) {
}
