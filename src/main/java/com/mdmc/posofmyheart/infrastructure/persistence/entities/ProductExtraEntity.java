package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_extras", indexes = {
        @Index(name = "idx_extra_name", columnList = "name"),
        @Index(name = "idx_extra_active", columnList = "active"),
        @Index(name = "idx_extra_price", columnList = "actual_price"),
        @Index(name = "idx_extra_cost", columnList = "actual_cost")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductExtraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extra")
    private Long idExtra;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "actual_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal actualPrice;

    @Column(name = "actual_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal actualCost;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "image")
    private String image;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "productExtra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderExtrasDetailEntity> extraDetails = new ArrayList<>();

}
