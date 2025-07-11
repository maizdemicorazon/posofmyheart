package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_flavor_detail", indexes = {
        @Index(name = "idx_order_flavor_detail", columnList = "id_order_detail"),
        @Index(name = "idx_order_flavor_flavor", columnList = "id_flavor")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFlavorDetailEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFlavorDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_detail", nullable = false)
    private OrderDetailEntity orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_flavor")
    private ProductFlavorEntity flavor;

    public OrderFlavorDetailEntity(OrderDetailEntity orderDetail, ProductFlavorEntity flavor) {
        this.orderDetail = orderDetail;
        this.flavor = flavor;
    }
}