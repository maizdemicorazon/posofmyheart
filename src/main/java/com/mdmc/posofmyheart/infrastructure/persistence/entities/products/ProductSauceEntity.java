package com.mdmc.posofmyheart.infrastructure.persistence.entities.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_sauces", indexes = {
        @Index(name = "idx_product_sauce_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSauceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sauce")
    private Long idSauce;
    private String name;
    private String description;
    private String image;
}
