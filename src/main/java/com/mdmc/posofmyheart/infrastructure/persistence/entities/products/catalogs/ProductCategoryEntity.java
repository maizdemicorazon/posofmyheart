package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.AuditEntity;

@Entity
@Table(name = "product_categories", indexes = {
        @Index(name = "idx_category_name", columnList = "name")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long idCategory;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
