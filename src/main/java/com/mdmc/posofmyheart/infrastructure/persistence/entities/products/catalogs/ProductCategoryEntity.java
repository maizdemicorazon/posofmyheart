package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_categories", indexes = {
        @Index(name = "idx_category_name", columnList = "name")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long idCategory;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
