package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductSauceEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail_sauce", indexes = {
        @Index(name = "idx_order_sauce_composite", columnList = "id_order_detail,id_sauce", unique = true),
        @Index(name = "idx_sauce_order_composite", columnList = "id_sauce,id_order_detail"),
        @Index(name = "idx_order_detail_sauce_order", columnList = "id_order_detail"),
        @Index(name = "idx_order_detail_sauce_sauce", columnList = "id_sauce")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailSauceEntity {
    @EmbeddedId
    private OrderDetailSauceKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idOrderDetail")
    @JoinColumn(name = "id_order_detail")
    private OrderDetailEntity orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idSauce")
    @JoinColumn(name = "id_sauce")
    private ProductSauceEntity productSauce;

    public OrderDetailSauceEntity(OrderDetailEntity orderDetail, ProductSauceEntity productSauce) {
        this.id = new OrderDetailSauceKey(orderDetail.getIdOrderDetail(), productSauce.getIdSauce());
        this.orderDetail = orderDetail;
        this.productSauce = productSauce;
    }
}
