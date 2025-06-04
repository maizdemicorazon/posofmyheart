package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "order_details", indexes = {
        @Index(name = "idx_order_detail_order", columnList = "id_order"),
        @Index(name = "idx_order_detail_product", columnList = "id_product"),
        @Index(name = "idx_order_detail_variant", columnList = "id_variant"),
        @Index(name = "idx_order_detail_sell_price", columnList = "sell_price"),
        @Index(name = "idx_order_detail_production_cost", columnList = "production_cost")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private Long idOrderDetail;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "id_variant", nullable = false)
    private ProductVariantEntity variant;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailSauceEntity> sauceDetails;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderExtraDetailEntity> extraDetails;

    @Column(name = "sell_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellPrice;

    @Column(name = "production_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal productionCost;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderFlavorDetailEntity> flavorDetails;

    public void addExtraDetail(OrderExtraDetailEntity extraDetail) {
        extraDetails.add(extraDetail);
        extraDetail.setOrderDetail(this);
    }

    public void addSauce(ProductSauceEntity sauce) {
        OrderDetailSauceEntity detailSauce = new OrderDetailSauceEntity(this, sauce);
        sauceDetails.add(detailSauce);
    }

}
