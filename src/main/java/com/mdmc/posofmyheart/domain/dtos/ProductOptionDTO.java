package com.mdmc.posofmyheart.domain.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDTO {
    private String size;
    private BigDecimal price;
}
