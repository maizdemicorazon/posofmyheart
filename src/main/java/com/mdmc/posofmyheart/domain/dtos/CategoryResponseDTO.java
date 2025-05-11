package com.mdmc.posofmyheart.domain.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Integer idCategory;
    private String name;
    private List<ProductOptionDTO> options = new ArrayList<>();
}