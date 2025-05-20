package com.mdmc.posofmyheart.infrastructure.persistence.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailSauceEntity {
    @EmbeddedId
    private OrderDetailSauceKey orderDetailSauceKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderDetailId")
    @JoinColumn(name = "id_order_detail")
    private OrderDetailEntity orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sauceId")
    @JoinColumn(name = "id_sauce")
    private SauceEntity sauce;

}
