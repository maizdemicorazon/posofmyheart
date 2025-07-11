package com.mdmc.posofmyheart.domain.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyEarningsResponseDto {
    private LocalDate fecha;
    private Long ordenes;
    private BigDecimal ventaBruta;
    private BigDecimal gananciaProductosNeta;
    private BigDecimal gananciaExtrasNeta;
    private BigDecimal descuentoTerminal;
    private BigDecimal ventaEnTerminal;
    private BigDecimal gananciaReal;
}
