package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductFlavorEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_flavor_detail", indexes = {
        @Index(name = "idx_order_flavor_detail", columnList = "id_order_detail"),
        @Index(name = "idx_order_flavor_flavor", columnList = "id_flavor")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFlavorDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFlavorDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_detail", nullable = false)
    private OrderDetailEntity orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_flavor")
    private ProductFlavorEntity flavor;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public OrderFlavorDetailEntity(OrderDetailEntity orderDetail, ProductFlavorEntity flavor) {
        this.orderDetail = orderDetail;
        this.flavor = flavor;
    }
}