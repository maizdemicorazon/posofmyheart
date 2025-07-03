package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductExtraEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_extras_detail", indexes = {
        @Index(name = "idx_extra_detail_composite", columnList = "id_order_detail,id_extra", unique = true),
        @Index(name = "idx_extra_detail_order", columnList = "id_order_detail"),
        @Index(name = "idx_extra_detail_extra", columnList = "id_extra"),
        @Index(name = "idx_extra_detail_sell_price", columnList = "sell_price"),
        @Index(name = "idx_extra_detail_production_cost", columnList = "production_cost"),
        @Index(name = "idx_extra_order_composite", columnList = "id_extra,id_order_detail")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderExtraDetailEntity {
    @EmbeddedId
    private OrderExtraDetailKey id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idOrderDetail")
    @JoinColumn(name = "id_order_detail")
    private OrderDetailEntity orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idExtra")
    @JoinColumn(name = "id_extra")
    private ProductExtraEntity productExtra;

    @Column(name = "sell_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellPrice;

    @Column(name = "production_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal productionCost;

    public void setRelations(OrderDetailEntity orderDetail, ProductExtraEntity productExtra) {
        this.id = new OrderExtraDetailKey(orderDetail.getIdOrderDetail(), productExtra.getIdExtra());
        this.orderDetail = orderDetail;
        this.productExtra = productExtra;
    }
}