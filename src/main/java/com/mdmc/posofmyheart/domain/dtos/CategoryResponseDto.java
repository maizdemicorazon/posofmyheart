package com.mdmc.posofmyheart.domain.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Integer idCategory;
    private String name;
    private List<ProductOptionDto> options = new ArrayList<>();
}