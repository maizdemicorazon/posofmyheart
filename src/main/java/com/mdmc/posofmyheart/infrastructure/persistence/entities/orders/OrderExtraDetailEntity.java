package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;

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
public class OrderExtraDetailEntity extends BaseEntity {
    @EmbeddedId
    private OrderExtraDetailKey id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sell_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellPrice;

    @Column(name = "production_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal productionCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idOrderDetail")
    @JoinColumn(name = "id_order_detail")
    private OrderDetailEntity orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idExtra")
    @JoinColumn(name = "id_extra")
    private ProductExtraEntity productExtra;

    public void setRelations(OrderDetailEntity orderDetail, ProductExtraEntity productExtra) {
        this.id = new OrderExtraDetailKey(orderDetail.getIdOrderDetail(), productExtra.getIdExtra());
        this.orderDetail = orderDetail;
        this.productExtra = productExtra;
    }
}