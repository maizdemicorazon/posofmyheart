package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "id_order")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sauce", nullable = false)
    private SauceEntity sauce;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCost;
}