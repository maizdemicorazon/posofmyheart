package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_extras_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductExtrasDetailEntity {
    @EmbeddedId
    private ProductExtraDetailKey idExtraDetailKey;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @ManyToOne
    @MapsId("idOrderDetail")
    @JoinColumn(name = "id_order_detail", nullable = false)
    private OrderDetailEntity orderDetail;

}
