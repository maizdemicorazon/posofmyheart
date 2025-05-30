package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlavorOptions {
    @ManyToOne
    @JoinColumn(name = "id_product")
    private ProductEntity product;
    private String flavor;
}
