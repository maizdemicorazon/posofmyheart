package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_categories", indexes = {
        @Index(name = "idx_category_name", columnList = "name")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long idCategory;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
