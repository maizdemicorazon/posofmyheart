package com.mdmc.posofmyheart.infrastructure.persistence.entities.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailSauceKey implements Serializable {

    @Column(name = "id_order_detail")
    private Long idOrderDetail;

    @Column(name = "id_sauce")
    private Long idSauce;
}