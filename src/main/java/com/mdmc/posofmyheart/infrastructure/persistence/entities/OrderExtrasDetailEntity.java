package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_extras_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderExtrasDetailEntity {
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

    public void setRelations(OrderDetailEntity orderDetail, ProductExtraEntity productExtra) {
        this.orderDetail = orderDetail;
        this.productExtra = productExtra;
        this.id = new OrderExtraDetailKey(productExtra.getIdExtra(), orderDetail.getIdOrderDetail());
    }
}