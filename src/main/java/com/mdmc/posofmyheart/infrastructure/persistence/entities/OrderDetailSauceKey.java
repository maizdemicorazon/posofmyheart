package com.mdmc.posofmyheart.infrastructure.persistence.entities;

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
    private Long orderDetailId;

    @Column(name = "id_sauce")
    private Long sauceId;
}