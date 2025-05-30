package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductFlavorKey implements Serializable {

    @Column(name = "id_flavor")
    private Long idFlavor;

    @Column(name = "id_product")
    private Long idProduct;
}